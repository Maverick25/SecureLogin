package agency.alterway.edillion.controllers;

import java.util.List;

import agency.alterway.edillion.EdillionApplication;
import agency.alterway.edillion.controllers.injections.MainInjection;
import agency.alterway.edillion.db.DatabaseHandler;
import agency.alterway.edillion.models.DocumentFile;

/**
 * Created by marekrigan on 26/05/15.
 */
public class MainController
{
    private static MainController instance;
    private static MainInjection injection;

    private MainController(MainInjection injection)
    {
        MainController.injection = injection;
    }

    public static MainController getInstance(MainInjection injection)
    {
        if(instance == null)
        {
            instance = new MainController(injection);
        }
        else
        {
            MainController.injection = injection;
        }
        return instance;
    }

    public void getDocumentFiles()
    {
        try
        {
            List<DocumentFile> documents = DatabaseHandler.getInstance(EdillionApplication.getAppContext()).getAllDocuments();

            injection.onReceivedDocuments(documents);
        }
        catch(Exception e)
        {
            injection.onErrorMain(e.getMessage());
        }

    }

}
