package hu.schonherz.javatraining.issuetracker.web.beans;

import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SessionScoped
@ManagedBean(name = "userSessionBean")
public class UserSessionBean {

    @EJB
    private UserServiceRemote userService;

    private UserVo currentUser;
    private String userName;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        userName = request.getUserPrincipal().getName();
        currentUser = userService.findByUsername(userName);
    }

    public boolean isUser() {
        List<RoleVo> roles = currentUser.getRoles();
        for (RoleVo role : roles) {
            String roleName = role.getName();
            if ("ROLE_USER".equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isManager() {
        List<RoleVo> roles = currentUser.getRoles();
        for (RoleVo role : roles) {
            String roleName = role.getName();
            if ("ROLE_MANAGER".equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin() {
        List<RoleVo> roles = currentUser.getRoles();
        for (RoleVo role : roles) {
            String roleName = role.getName();
            if ("ROLE_ADMIN".equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public UserServiceRemote getUserService() {
        return userService;
    }

    public void setUserService(UserServiceRemote userService) {
        this.userService = userService;
    }

    public UserVo getCurrentUser() {
        init();
        return currentUser;
    }

    public void setCurrentUser(UserVo currentUser) {
        this.currentUser = currentUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }




}
