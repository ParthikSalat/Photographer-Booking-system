/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restAPI;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author parthik
 */
@ApplicationPath("/api") // This sets the base URL path for all REST endpoints
public class RESTConfig extends Application {
    // No need to implement any methods, this is enough to activate JAX-RS
}