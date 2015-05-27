package agency.alterway.edillion.controllers;

import java.util.ArrayList;
import java.util.List;

import agency.alterway.edillion.R;
import agency.alterway.edillion.controllers.injections.MainInjection;
import agency.alterway.edillion.models.DocumentFile;

/**
 * Created by marekrigan on 26/05/15.
 */
public class MainController
{
    private static MainController instance;
    private MainInjection injection;

    private MainController(MainInjection injection)
    {
        this.injection = injection;
    }

    public static MainController getInstance(MainInjection injection)
    {
        if(instance == null)
        {
            instance = new MainController(injection);
        }
        return instance;
    }

    public void getDocumentFiles()
    {
        try
        {
            List<DocumentFile> documents = new ArrayList<>();
            DocumentFile species = new DocumentFile();
            species.setDescription("Internet");
            species.setThumbnail(R.drawable.bill1);
            documents.add(species);

            species = new DocumentFile();
            species.setDescription("Electricity");
            species.setThumbnail(R.drawable.bill2);
            documents.add(species);

            species = new DocumentFile();
            species.setDescription("Waste");
            species.setThumbnail(R.drawable.bill3);
            documents.add(species);

            injection.onReceivedDocuments(documents);
        }
        catch(Exception e)
        {
            injection.onErrorMain(e.getMessage());
        }

    }
}
