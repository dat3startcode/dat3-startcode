/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datafacades;

import entities.Child;
import entities.Parent;

import javax.persistence.EntityManagerFactory;

import entities.Toy;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        IDataFacade pf = ParentFacade.getParentFacade(emf);
        Parent p1 = new Parent("Henrik",76);
        Parent p2 = new Parent("Betty",76);
        Child c1 = new Child("Hassan", 10);
        Child c2 = new Child("Josephine",5);
        Toy t1 = new Toy("Chess board", 2);
        Toy t2 = new Toy("Lego Friends set",3);
        p1.addChild(c1);
        p1.addChild(c2);
        c1.addToy(t1);
        c1.addToy(t2);
        pf.create(p1);
        pf.create(p2);

    }
    
    public static void main(String[] args) {
        populate();
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        IDataFacade pf = ParentFacade.getParentFacade(emf);
        pf.getAll().forEach(System.out::println);
    }
}
