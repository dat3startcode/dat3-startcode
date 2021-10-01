/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.RenameMeDTO;
import entities.Parent;
import entities.RenameMe;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        ParentFacade pf = ParentFacade.getParentFacade(emf);
        pf.create(new Parent("Henrik",76));
        pf.create(new Parent("Betty",76));

    }
    
    public static void main(String[] args) {
        populate();
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        ParentFacade pf = ParentFacade.getParentFacade(emf);
        pf.getAll().forEach(System.out::println);
    }
}
