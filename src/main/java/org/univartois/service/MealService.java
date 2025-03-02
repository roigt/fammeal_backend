package org.univartois.service;

import jakarta.ws.rs.core.Response;
import org.univartois.dto.request.MealRequestDto;
import org.univartois.dto.response.MealResponseDto;
import org.univartois.dto.response.MealResponseFromDateToDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MealService {

    /**
     * Récupère les repas planifiés pour une maison donnée.
     * @param homeId
     * @return List<MealResponseDto>
     */
    List<MealResponseDto> getMealsByHome(UUID homeId);

    MealResponseDto getMealsByHomeAndIdMeal(UUID homeId,UUID idMeal);

    MealResponseFromDateToDto getMealDateTo(UUID idHome, LocalDate from, LocalDate to);

    /**
     * Planifie un repas pour une maison.
     * @param homeId
     * @param mealRequestDto
     * @return MealResponseDto
     */
    MealResponseDto planMeal(UUID homeId, MealRequestDto mealRequestDto);

    /**
     * Met à jour un repas existant pour une maison donnée.
     * @param homeId
     * @param mealId
     * @param mealRequestDto
     * @return MealResponseDto
     */
    MealResponseDto updateMeal(UUID homeId, UUID mealId, MealRequestDto mealRequestDto);

    /**
     * Supprime un repas planifié.
     * @param homeId
     * @param mealId
     */
    void deleteMeal(UUID homeId, UUID mealId);
}
