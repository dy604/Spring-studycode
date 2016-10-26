package study.springframework.beans.factory.support;

import java.security.AccessControlContext;

/**
 * Created by pc on 2016/10/13.
 */
public interface SecurityContextProvider {

    AccessControlContext getAccessControlContext();
}
