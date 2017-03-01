package hu.schonherz.javatraining.issuetracker.web.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.schonherz.javatraining.issuetracker.client.api.service.user.DefaultUserConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.shared.AdminJNDIConstants;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.project.remote.admin.api.rpc.issuetracker.RemoteLoginService;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteUserVo;
import lombok.extern.log4j.Log4j;

@Service("customUserDetailsService")
@Log4j
public class CustomUserDetailsService implements UserDetailsService  {
	
    @EJB
    private UserServiceRemote userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        UserVo user;
        try {
        	if (username.equals(DefaultUserConstants.USER_ADMIN)
        		|| username.equals(DefaultUserConstants.USER_MANAGER)
        		|| username.equals(DefaultUserConstants.USER_USER)) {
        		throw new NameNotFoundException();
        	}
        	
        	Context context = new InitialContext();
        	log.debug("get login service context");
        	RemoteLoginService adminLoginService = (RemoteLoginService) context.lookup(AdminJNDIConstants.JNDI_LOGIN_SERVICE);
        	log.debug("get login info from admin");
        	RemoteUserVo remoteUserVo = adminLoginService.login(username);
        	log.debug("map remote to uservo");
        	user = userService.mapRemoteUserVoToUserVo(remoteUserVo);
        	
        	if (user == null) {
            	throw new UsernameNotFoundException(username);
            }
        	
        	if (user.getId() == null) {
        		user = userService.save(user);
        	}
        	
        	return login(user);
        } catch (NameNotFoundException e) {
        	log.debug("get login info from issue database");
        	user = userService.findByUsername(username);
        	if (user == null) {
            	throw new UsernameNotFoundException(username);
            }
        	
        	return login(user);
        } catch (Exception e) {
            log.error(e);
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    private UserDetails login(UserVo user) {
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }
    
    private User buildUserForAuthentication(UserVo user, List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(List<RoleVo> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (RoleVo userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
        }
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
        return result;
    }
}