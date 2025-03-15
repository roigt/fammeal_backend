package org.univartois.service.impl;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.univartois.dto.request.IngredientRequestDto;
import org.univartois.dto.response.IngredientResponseDto;
import org.univartois.entity.IngredientEntity;
import org.univartois.enums.IngredientUnit;
import org.univartois.exception.IngredientAlreadyExistsException;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.IngredientMapper;
import org.univartois.repository.IngredientRepository;
import org.univartois.service.IngredientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@ApplicationScoped
public class IngredientServiceImpl implements IngredientService {

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    IngredientMapper ingredientMapper;
    @Inject
    EntityManager entityManager;

    /**
     * Creer un ingrédient
     * @param IngredientRequestDto
     * @return
     */
    @Override
    @Transactional
    public IngredientResponseDto createIngredient(IngredientRequestDto IngredientRequestDto) {
//        Optional<IngredientEntity> existingIngredient = Optional.ofNullable(ingredientRepository.findByIngredientName(IngredientRequestDto.getIngredientName())
//                .orElseThrow(() -> new ResourceNotFoundException("Ingrédient non trouvé")));
         Optional<IngredientEntity> existingIngredient = ingredientRepository.findByIngredientName(IngredientRequestDto.getIngredientName());

        IngredientUnit unit = IngredientRequestDto.getIdUnit();

        if (existingIngredient.isPresent()) {
            throw new IngredientAlreadyExistsException("Un ingrédient avec le nom '" + IngredientRequestDto.getIngredientName() + "' existe déjà.");

        }else{

            IngredientEntity ingredientEntity = ingredientMapper.toEntity(IngredientRequestDto);
            ingredientEntity.setIdUnit(unit);
            ingredientRepository.persist(ingredientEntity);

            return ingredientMapper.toResponseDto(ingredientEntity);

        }



    }

    /**
     * Récupérer un ingrédient grâce à son id
     * @param ingredientId
     * @return
     */
    @Override
    public IngredientResponseDto getIngredientById(UUID ingredientId) {
        IngredientEntity ingredientEntity = ingredientRepository.findByIdOptional(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrédient non trouvé"));
        return ingredientMapper.toResponseDto(ingredientEntity);
    }

    /**
     * Récupérer tous les ingrédients
     * @return
     */
    @Override
    public List<IngredientResponseDto> getAllIngredients() {
//        List<IngredientEntity> ingredientEntities = ingredientRepository.findAllIngredients();
        return ingredientRepository.findAll().stream()
                .map(ingredient -> new IngredientResponseDto(
                        ingredient.getIdUnit(),
                        ingredient.getIngredientName(),
                        ingredient.isIngredientIsVegan(),
                        ingredient.getNbDayBeforeExpiration(),
                        ingredient.getIdIngredient(),
                        ingredient.getAllAllergies()
                ))
                .toList();
    }

    @Override
    public List<IngredientResponseDto> getVeganIngredients() {
        List<IngredientEntity> ingredientEntities = ingredientRepository.findByVeganStatus(true);
        return ingredientMapper.toResponseDtoList(ingredientEntities);
    }

    @Override
    public List<IngredientResponseDto> getIngredientsByExpiration(int days) {
        List<IngredientEntity> ingredientEntities = ingredientRepository.findByExpirationDateBefore(days);
        return ingredientMapper.toResponseDtoList(ingredientEntities);
    }

    /**
     * Modifier un ingrédient grâce à son Id
     * @param ingredientId
     * @param ingredientRequestDto
     * @return
     */
    @Override
    @Transactional
    public IngredientResponseDto updateIngredient(UUID ingredientId, IngredientRequestDto ingredientRequestDto) {

        Optional<IngredientEntity> existingIngredient = ingredientRepository.findByIdOptional(ingredientId);


        if (existingIngredient.isEmpty()) {
            throw new ResourceNotFoundException("Ingrédient non trouvé.");
        }

        IngredientEntity ingredientEntity = ingredientMapper.toEntity(ingredientRequestDto);
        existingIngredient.get().setIngredientName(ingredientRequestDto.getIngredientName());
        existingIngredient.get().setNbDayBeforeExpiration(ingredientRequestDto.getNbDayBeforeExpiration());
        existingIngredient.get().setIngredientIsVegan(ingredientRequestDto.isIngredientIsVegan());
        existingIngredient.get().setIdIngredient(ingredientId);
        ingredientRepository.persist(existingIngredient.get());
        return ingredientMapper.toResponseDto(existingIngredient.get());
    }

    /**
     * Supprimer un ingrédient grâce à son id
     * @param ingredientId
     */
    @Override
    @Transactional
    public void deleteIngredient(UUID ingredientId) {

        Optional<IngredientEntity> ingredient = ingredientRepository.findByIdOptional(ingredientId);

        if (ingredient.isEmpty()) {
            throw new ResourceNotFoundException("Ingrédient non trouvé.");
        }

        ingredientRepository.delete(ingredient.get());
//        if(ingredientRepository.isPersistent(ingredient.get())){
//            ingredientRepository.delete(ingredient.get());
//        }

    }

}
