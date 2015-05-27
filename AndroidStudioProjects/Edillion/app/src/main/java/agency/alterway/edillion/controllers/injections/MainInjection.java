package agency.alterway.edillion.controllers.injections;

import java.util.List;

import agency.alterway.edillion.models.DocumentFile;

/**
 * Created by marekrigan on 26/05/15.
 */
public interface MainInjection
{
    void onReceivedDocuments(List<DocumentFile> documents);

    void onErrorMain(String message);
}
