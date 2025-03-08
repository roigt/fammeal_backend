package org.univartois.service;

import jakarta.transaction.Transactional;
import org.univartois.dto.request.ProposedMealRequestDto;

import org.univartois.dto.response.MealProposalsByDateResponse;
import org.univartois.dto.response.ProposedMealResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProposedMealService {


    List<ProposedMealResponseDto> getAllProposedMeals(UUID homeId);

    @Transactional
    ProposedMealResponseDto proposeMeal(UUID homeId,ProposedMealRequestDto proposedMealRequestDto);

    List<ProposedMealResponseDto> getProposedMealsByMealId(UUID homeId,UUID mealId);

    @Transactional
    ProposedMealResponseDto updateProposedMeal(UUID recipeId, UUID mealId, UUID proposerId, ProposedMealRequestDto proposedMealRequestDto);

    @Transactional
    MealProposalsByDateResponse searchMealByDate(UUID homeId, LocalDate date);

    @Transactional
    void deleteProposedMeal(UUID homeId,ProposedMealRequestDto proposedMealRequestDto);
}
