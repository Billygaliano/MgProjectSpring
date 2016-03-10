/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgProject.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import mgProject.collection.Project;
import mgProject.collection.User;
import mgProject.service.ProjectService;
import mgProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author alejandroruiz
 */
@Component
@Scope("request")
public class ProjectListBean implements Serializable{
    @Autowired
    private LoginBean loginBean;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProjectService projectService;

    private List<Project> list = new ArrayList<>();
    private boolean error=false;
    private List<Project> listCollaborators;
    

    /**
     * Creates a new instance of projectListBean
     */
    public ProjectListBean() {
    }

    public List<Project> getListCollaborators() {
        return listCollaborators;
    }

    public void setListCollaborators(List<Project> listCollaborators) {
        this.listCollaborators = listCollaborators;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Project> getList() {
        return list;
    }

    public void setList(List<Project> list) {
        this.list = list;
    }
    
    @PostConstruct
    public void init(){
        User user = userService.findUserById(loginBean.getIdUser());
        List<String> listIdProjects = user.getProjects();
        
        if (listIdProjects != null){
            for (String listIdProject : listIdProjects) {
                System.out.println("Proyecto: " + listIdProject);
            }
        }

        if(listIdProjects != null){
            for (String listIdProject : listIdProjects) {
                if(projectService.findProjectById(listIdProject).getIdAdmin().equals(loginBean.getIdUser())){
                    System.out.println("Proyecto tal: " + projectService.findProjectById(listIdProject));
                    list.add(projectService.findProjectById(listIdProject));
                    loginBean.setProject_list(list);
                }else{
                    listCollaborators.add(projectService.findProjectById(listIdProject));
                    loginBean.setProject_list(listCollaborators);
                }
            }
        }
        
        if(list == null || list.isEmpty()){
            error=true;
        }
    }
    
}
