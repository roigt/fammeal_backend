package org.univartois.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class MealProposalsByDateResponse {
    private mealsPrototype lunch;
    private mealsPrototype diner;


    @Data
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class mealsPrototype{
        public int nbPropositions;
        public List<ProposedMealResponseDto> mealList = new ArrayList<>();

    }

}
