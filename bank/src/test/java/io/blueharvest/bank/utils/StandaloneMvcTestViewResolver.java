package io.blueharvest.bank.utils;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author Mohamed Morsey
 * Date: 2018-10-10
 **/
public class StandaloneMvcTestViewResolver extends InternalResourceViewResolver {
    @Override
    protected AbstractUrlBasedView buildView(final String viewName) throws Exception {
        final InternalResourceView view = (InternalResourceView) super.buildView(viewName);

        // Stop check for circular views
        view.setPreventDispatchLoop(false);
        return view;
    }
}
