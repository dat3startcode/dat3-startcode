package facades;

import dtos.ParentDTO;
import entities.Parent;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class RestFacade implements IDataFacade<ParentDTO> {
    private static IDataFacade<ParentDTO> instance;
    private static IDataFacade<Parent> parentFacade;

    //Private Constructor to ensure Singleton
    private RestFacade() {}

    public static IDataFacade<ParentDTO> getRestFacade() {
        if (instance == null) {
             parentFacade = ParentFacade.getParentFacade(EMF_Creator.createEntityManagerFactory());
            instance = new RestFacade();
        }
        return instance;
    }

    @Override
    public ParentDTO create(ParentDTO parentDTO) {
        Parent p = parentDTO.getEntity();
        p = parentFacade.create(p);
        return new ParentDTO(p);
    }

    @Override
    public ParentDTO getById(int id) throws EntityNotFoundException {
        return new ParentDTO(parentFacade.getById(id));
    }

    @Override
    public List<ParentDTO> getAll() {
        return ParentDTO.toList(parentFacade.getAll());
    }

    @Override
    public ParentDTO update(ParentDTO parentDTO) throws EntityNotFoundException {
        Parent p = parentFacade.update(parentDTO.getEntity());
        return new ParentDTO(p);
    }

    @Override
    public ParentDTO delete(int id) throws EntityNotFoundException {
        return new ParentDTO(parentFacade.delete(id));
    }
}
