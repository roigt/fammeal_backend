package org.univartois.dto.request;
import jakarta.validation.Valid;
import org.jboss.resteasy.reactive.PartType;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import java.io.InputStream;

public class RecipeMultipartForm {

    @FormParam("file")
    @PartType(MediaType.MULTIPART_FORM_DATA)
    public InputStream file;

    @FormParam("recipeRequest")
    @PartType(MediaType.APPLICATION_JSON)
    @Valid
    public RecipeRequestDto recipeRequest;
}
