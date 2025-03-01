package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import org.eclipse.microprofile.jwt.JsonWebToken;
import org.univartois.dto.request.PantryIngredientRequestDto;
import org.univartois.dto.response.PantryIngredientResponseDto;
import org.univartois.entity.HomeEntity;
import org.univartois.entity.IngredientEntity;
import org.univartois.entity.PantryIngredientEntity;
import org.univartois.entity.UserEntity;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.PantryIngredientMapper;
import org.univartois.repository.HomeRepository;
import org.univartois.repository.IngredientRepository;
import org.univartois.repository.PantryIngredientRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.PantryIngredientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ApplicationScoped
public class PantryIngredientServiceImpl implements PantryIngredientService {

    @Inject
    HomeRepository homeRepository;
    @Inject
    PantryIngredientRepository pantryIngredientRepository;

    @Inject
    PantryIngredientMapper pantryIngredientMapper;

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JsonWebToken jwt;

    /**
     * Ajouter un ingrédient au pantryIngrédients
     *
     * @param pantryIngredientRequestDto
     * @return
     */
    @Override
    @Transactional
    public PantryIngredientResponseDto addPantryIngredient(UUID homeId,PantryIngredientRequestDto pantryIngredientRequestDto) {

        UUID userId = UUID.fromString(jwt.getSubject());


        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));


        IngredientEntity ingredient = ingredientRepository.findById(pantryIngredientRequestDto.getId_ingredient())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //on essaie de recuperer l ingredient grace a son id dans le garde-manger de la maison
        Optional<PantryIngredientEntity> pantryIngredientSearch = pantryIngredientRepository.findByIdIngredientPantryAndHomeId(pantryIngredientRequestDto.getId_ingredient(),
             homeId);


        if (pantryIngredientRequestDto.getIngredientExpirationDate().isBefore(LocalDate.now())) {
            throw new ResourceNotFoundException("La date  d' expiration de l ingredient est inferieur a la date du jour -> expirationDate ");
        }

        if (pantryIngredientSearch.isPresent()) {

            //si l ingredient existe on le met a jour

            pantryIngredientSearch.get().setIngredientQuantity(pantryIngredientSearch.get().getIngredientQuantity() + pantryIngredientRequestDto.getIngredientQuantity());
            pantryIngredientSearch.get().setIngredientExpirationDate(pantryIngredientRequestDto.getIngredientExpirationDate());


            PantryIngredientEntity result = pantryIngredientMapper.toEntity(pantryIngredientRequestDto);

            return pantryIngredientMapper.toResponseDto(result);

        } else {
            PantryIngredientEntity pantryIngredient = pantryIngredientMapper.toEntity(pantryIngredientRequestDto);
            pantryIngredient.setHome(home);
            pantryIngredient.setIngredient(ingredient);
            pantryIngredient.setUser(user);

            pantryIngredientRepository.persist(pantryIngredient);


            return pantryIngredientMapper.toResponseDto(pantryIngredient);

        }


    }



    /**
     * Récupérer les ingrédients d'une maison à partir de id de la maison
     *
     * @param homeId (id de la maison)
     * @return (retourne la liste des ingrédients de cette maison)
     */
    @Override
    @Transactional
    public List<PantryIngredientResponseDto> getPantryIngredientsByHomeId(UUID homeId) {
        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));

        List<PantryIngredientEntity> pantryIngredients = pantryIngredientRepository.findByHome(homeId);
//        pantryIngredients.forEach(entity -> {
//            IngredientResponseDto responseIngredientClient = ingredientClient.getIngredientById(entity.getIngredient().getIdIngredient());
//
//            IngredientEntity ingredientEntity = new IngredientEntity();
//
//            ingredientEntity.setIngredientName(responseIngredientClient.getIngredientName());
//            ingredientEntity.setIngredientIsVegan(responseIngredientClient.isIngredientIsVegan());
//            ingredientEntity.setNbDayBeforeExpiration(responseIngredientClient.getNbDayBeforeExpiration());
//
//            entity.setIngredientDetails(ingredientEntity);
//        });


        return pantryIngredientMapper.toResponseDtoList(pantryIngredients);
    }

    /**
     * Récuperer un les infos sur un ingredient précis du garde manger d'une maison grace a id de la maison
     * et de l idPantryIngredient
     * @param homeId
     * @param pantryIngredientId
     * @return
     */
    @Transactional
    @Override
    public PantryIngredientResponseDto getPantryIngredientByPantryIngredientIdAndHomeId(UUID homeId, UUID pantryIngredientId) {
        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));

        PantryIngredientEntity pantryIngredients = pantryIngredientRepository. findByIdPantryIngredientAndHomeId(pantryIngredientId,homeId);


        return  pantryIngredientMapper.toResponseDto(pantryIngredients);
    }





    /**
     * Mise à jour d'un ingrédient dans le garde-manger
     *
     * @param ingredientInPantryId
     * @param pantryIngredientRequestDto
     * @return
     */
    @Override
    @Transactional
    public PantryIngredientResponseDto updatePantryIngredient(UUID ingredientInPantryId, PantryIngredientRequestDto pantryIngredientRequestDto) {

        Optional<PantryIngredientEntity> existingPantryIngredient = pantryIngredientRepository.findByIdPantryIngredient(ingredientInPantryId);

        if (pantryIngredientRequestDto.getIngredientExpirationDate().isBefore(LocalDate.now())) {
            throw new ResourceNotFoundException("La date  d' expiration de l ingredient est inferieur a la date du jour -> expirationDate ");
        }

        if (existingPantryIngredient.isEmpty()) {
            throw new ResourceNotFoundException("Ingrédient  non trouvé dans le garde-manger.");
        }

        PantryIngredientEntity pantryIngredientEntity = pantryIngredientMapper.toEntity(pantryIngredientRequestDto);

        existingPantryIngredient.get().setIngredientQuantity(pantryIngredientRequestDto.getIngredientQuantity());

        existingPantryIngredient.get().setIngredientExpirationDate(pantryIngredientRequestDto.getIngredientExpirationDate());

        // mettre à jour homeId
        //  pantryIngredientEntity.setHome(new HomeEntity(pantryIngredientRequestDto.getId_home()));


        pantryIngredientRepository.persist(existingPantryIngredient.get());


        return pantryIngredientMapper.toResponseDto(existingPantryIngredient.get());
    }

    /**
     * Supprimé un ingredient du garde-manger
     * @param ingredientInPantryId
     */
    @Override
    @Transactional
    public void deletePantryIngredient(UUID ingredientInPantryId) {
        PantryIngredientEntity existingPantryIngredient = pantryIngredientRepository.findByIdPantryIngredient(ingredientInPantryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrédient non trouvé dans le garde-manger."));

        pantryIngredientRepository.delete(existingPantryIngredient);
    }




}
