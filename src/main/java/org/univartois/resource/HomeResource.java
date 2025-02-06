package org.univartois.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api/homes")
public class HomeResource {


    @GET
    public String getHomes(){
        return "home alone";
    }
}
