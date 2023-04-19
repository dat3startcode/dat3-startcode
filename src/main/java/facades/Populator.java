/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.RenameMeDTO;
import entities.Hobby;
import entities.Person;
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
        PersonFacade pf = PersonFacade.getPersonFacade(emf);
        PersonDTO p1 = new PersonDTO(new Person("Holger", 3));
        p1 = pf.create(p1);
        pf.create(new PersonDTO(new Person("Hassan", 3)));
        pf.create(new PersonDTO(new Person("Hanne", 3)));
        HobbyDTO hdto1 = new HobbyDTO(new Hobby("Hobby1", "Hobby1"));
        HobbyDTO hdto2 = new HobbyDTO(new Hobby("Hobby2", "Hobby2"));
        hdto1 = pf.createHobby(hdto1);
//        hdto2 = pf.createHobby(hdto2);
        pf.addHobby(p1.getId(), hdto1.getId());
        pf.removeHobbyFromPerson(p1.getId(), hdto1.getId());
    }
    
    public static void main(String[] args) {
        populate();
    }
}
