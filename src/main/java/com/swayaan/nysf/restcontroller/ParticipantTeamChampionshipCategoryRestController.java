package com.swayaan.nysf.restcontroller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.DTO.AgeCategoryDTO;
import com.swayaan.nysf.entity.DTO.AsanaCategoryDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipService;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ParticipantTeamChampionshipCategoryRestController {

	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	AgeCategoryService ageCategoryService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ParticipantTeamChampionshipCategoryRestController.class);

	@GetMapping("/championship/{championshipId}/getAsanaCategory")
	public Set<AsanaCategoryDTO> getAsanaCategoryForChampionship(@PathVariable(name = "championshipId") Integer id,
			Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered getAsanaCategoryForChampionship ParticipantTeamChampionshipCategoryController ");
		Championship championship;
		Set<AsanaCategory> listAsanaCateogry = new HashSet<AsanaCategory>();
		Set<AsanaCategoryDTO> listAsanaCategoryDTO = new HashSet<>();
		try {
			championship = championshipService.get(id);
			List<ChampionshipCategory> listChampionshipCateogry = championship.getChampionshipCategory();
			if (!listChampionshipCateogry.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCateogry) {
					// listAsanaCateogry.add(championshipCategory.getAsanaCategory());
					AsanaCategoryDTO asanaCategoryDTO = new AsanaCategoryDTO(
							championshipCategory.getAsanaCategory().getId(),
							championshipCategory.getAsanaCategory().getName());
					if (!containsName(listAsanaCategoryDTO, asanaCategoryDTO.getName())) {
						listAsanaCategoryDTO.add(asanaCategoryDTO);
					}

				}
			}
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listAsanaCategoryDTO;
	}

	// Get only traditional asana category for compulsory asanas
	@GetMapping("/championship/{championshipId}/getTraditionalAsanaCategory")
	public Set<AsanaCategoryDTO> getTraditionalAsanaCategoryForChampionship(
			@PathVariable(name = "championshipId") Integer id, Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered getAsanaCategoryForChampionship ParticipantTeamChampionshipCategoryController ");
		Championship championship;
		Set<AsanaCategory> listAsanaCateogry = new HashSet<AsanaCategory>();
		Set<AsanaCategoryDTO> listAsanaCategoryDTO = new HashSet<>();
		try {
			championship = championshipService.get(id);
			List<ChampionshipCategory> listChampionshipCateogry = championship.getChampionshipCategory();
			if (!listChampionshipCateogry.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCateogry) {
					// listAsanaCateogry.add(championshipCategory.getAsanaCategory());
					if (championshipCategory.getAsanaCategory().getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
						AsanaCategoryDTO asanaCategoryDTO = new AsanaCategoryDTO(
								championshipCategory.getAsanaCategory().getId(),
								championshipCategory.getAsanaCategory().getName());
						if (!containsName(listAsanaCategoryDTO, asanaCategoryDTO.getName())) {
							listAsanaCategoryDTO.add(asanaCategoryDTO);
						}

					}
				}
			}
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listAsanaCategoryDTO;
	}

	public boolean containsName(Set<AsanaCategoryDTO> listAsanaCategoryDTO, String name) {
		return listAsanaCategoryDTO.stream().anyMatch(asanaCategoryDTO -> asanaCategoryDTO.getName().equals(name));
	}

	@GetMapping("/championship/{championshipId}/AsanaCategory/{asanaCategoryId}/getGender")
	public Set<String> getGenderForChampionshipAsanaCategory(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId, Model model,
			RedirectAttributes redirectAttributes) throws AsanaCategoryNotFoundException {
		LOGGER.info("Entered getGenderForChampionshipAsanaCategory ParticipantTeamChampionshipCategoryController ");
		Championship championship;
		Set<String> listGender = new HashSet<String>();
		try {
			championship = championshipService.get(id);
			AsanaCategory asanaCategory = asanaCategoryService.getAsanaCategoryById(asanaCategoryId);
			List<ChampionshipCategory> listChampionshipCateogry = championship.getChampionshipCategory();
			for (ChampionshipCategory championshipCategory : listChampionshipCateogry) {
				if (championshipCategory.getAsanaCategory().equals(asanaCategory)) {
					listGender.add(championshipCategory.getGender().toString());
				}

			}
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listGender;
	}

	@GetMapping("/championship/{championshipId}/AsanaCategory/{asanaCategoryId}/Gender/{gender}/getAgeCategory")
	public Set<AgeCategoryDTO> getAgeCategoryForChampionshipAsanaCategoryGender(
			@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException {
		LOGGER.info("Entered getGenderForChampionshipAsanaCategory ParticipantTeamChampionshipCategoryController ");
		Championship championship;
		Set<AgeCategory> listAgeCategory = new HashSet<AgeCategory>();
		Set<AgeCategoryDTO> listAgeCategoryDTO = new HashSet<>();
		try {
			championship = championshipService.get(id);
			AsanaCategory asanaCategory = asanaCategoryService.getAsanaCategoryById(asanaCategoryId);
			List<ChampionshipCategory> listChampionshipCateogry = championship.getChampionshipCategory();
			for (ChampionshipCategory championshipCategory : listChampionshipCateogry) {
				if (championshipCategory.getAsanaCategory().equals(asanaCategory)
						&& championshipCategory.getGender().equals(GenderEnum.valueOf(gender))) {
					System.out.println(true);
					// listAgeCategory.add(championshipCategory.getCategory());
					listAgeCategoryDTO.add(new AgeCategoryDTO(championshipCategory.getCategory().getId(),
							championshipCategory.getCategory().getTitle()));
				}

			}
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listAgeCategoryDTO;
	}

	@GetMapping("/championship/{championshipId}/AsanaCategory/{asanaCategoryId}/Gender/{gender}/AgeCategory/{ageCategoryId}/getRounds")
	public Set<Integer> getRoundForChampionshipAsanaCategoryGenderAgeCategory(
			@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException, EntityNotFoundException {
		LOGGER.info(
				"Entered getRoundForChampionshipAsanaCategoryGenderAgeCategory ParticipantTeamChampionshipCategoryController ");
		Championship championship;
		Set<Integer> listRounds = new HashSet<Integer>();
		try {
			championship = championshipService.get(id);
			AsanaCategory asanaCategory = asanaCategoryService.getAsanaCategoryById(asanaCategoryId);
			AgeCategory ageCategory = ageCategoryService.get(ageCategoryId);
			List<ChampionshipCategory> listChampionshipCateogry = championship.getChampionshipCategory();
			for (ChampionshipCategory championshipCategory : listChampionshipCateogry) {
				if (championshipCategory.getAsanaCategory().equals(asanaCategory)
						&& championshipCategory.getGender().equals(GenderEnum.valueOf(gender))
						&& championshipCategory.getCategory().equals(ageCategory)) {
					System.out.println(true);
					Integer noOfRounds = championshipCategory.getNoOfRounds();
					for (int i = 1; i <= noOfRounds; i++) {
						listRounds.add(i);
					}

				}

			}
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listRounds;
	}

}
