package org.univartois.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealResponseFromDateToDto {

    private Map<LocalDate, DailyMealsDto> meals;


    @Getter
    @Setter
    public static class DailyMealsDto {
        private List<MealDto> lunch = new ArrayList<>();
        private List<MealDto> diner = new ArrayList<>();

    }


    @Getter
    @Setter
    public static class MealDto {

        private UUID id_recipe;
        private String name;
        private String image_url;

    }
}
