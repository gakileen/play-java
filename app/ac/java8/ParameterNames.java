package ac.java8;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by acmac on 2016/09/08.
 */
public class ParameterNames {
    public static void main(String[] args) throws Exception {
        Method method = ParameterNames.class.getMethod( "main", String[].class );
        for( final Parameter parameter: method.getParameters() ) {
            System.out.println( "Parameter: " + parameter.getName() );
        }
    }
}