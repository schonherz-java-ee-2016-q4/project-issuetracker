package hu.schonherz.javatraining.issuetracker.web.view;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import lombok.extern.log4j.Log4j;


@ManagedBean(name = "languageBean")
@SessionScoped
@Log4j
public class LanguageBean {

    private static final String PROPERTIES_FILE_NAME = "Messages";

    private Locale locale;
    private ResourceBundle localeMessages;

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        updateLocaleMessages();
    }

    public void setLanguage(final String language) {
        Locale facesLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        log.warn("facesLocale before: " + facesLocale.getLanguage());
        if (!locale.getLanguage().equals(language)) {
            locale = new Locale(language);
            updateLocaleMessages();
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public String localize(final String key, final String... params) {
        if (params.length == 0) {
            return localeMessages.getString(key);
        }

        String pattern = getLocalizedPattern(key);
        return format(pattern, params);
    }

    private String getLocalizedPattern(final String key) {
        return localeMessages.getString(key);
    }

    private String format(final String pattern, final String... params) {
        MessageFormat formatter = new MessageFormat(pattern, getLocale());
        return formatter.format(params);
    }

    private void updateLocaleMessages() {
        localeMessages = ResourceBundle.getBundle(PROPERTIES_FILE_NAME, locale);
    }

}
