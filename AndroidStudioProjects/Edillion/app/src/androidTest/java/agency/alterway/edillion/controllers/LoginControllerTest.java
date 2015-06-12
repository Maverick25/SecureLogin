package agency.alterway.edillion.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import agency.alterway.edillion.controllers.injections.LoginInjection;

/**
 * Created by marekrigan on 11/06/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class LoginControllerTest
{
    @Mock
    private LoginInjection injection;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        doAnswer((new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                ArrayList<FitnessEntry> items = new ArrayList<>();
                items.add(entryMock);

                ((IFitnessListPresenterCallback) presenter).onFetchAllSuccess(items);
                return null;
            }
        })).when(model).fetchAllItems((IFitnessListPresenterCallback) presenter);
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testDoAuthorization() throws Exception
    {

    }
}