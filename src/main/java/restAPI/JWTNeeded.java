/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restAPI;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.ws.rs.NameBinding;
/**
 *
 * @author parthik
 */

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface JWTNeeded {}
