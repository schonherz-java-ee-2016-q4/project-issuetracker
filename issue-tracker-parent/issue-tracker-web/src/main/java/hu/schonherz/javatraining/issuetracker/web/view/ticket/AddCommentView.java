package hu.schonherz.javatraining.issuetracker.web.view.ticket;

import lombok.Data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "addCommentView")
@ViewScoped
@Data
public class AddCommentView {
    private String commentText;
}
