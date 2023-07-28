package com.swayaan.nysf.restcontroller;

import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO2;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipService;

@RestController
public class ChampionshipCategoryRestController {

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipCategoryRestController.class);

	@GetMapping("/championship/{championshipId}/getCategories")
	public List<ChampionshipCategoryDTO2> getChampionshipCategoryForChampionship(
			@PathVariable(name = "championshipId") Integer championshipId, Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered getChampionshipCategoryForChampionship ChampionshipCategoryRestController");
		Championship championship;
		
		List<ChampionshipCategoryDTO2> listChampionshipCategoryDTO = new ArrayList<>();
		try {
			championship = championshipService.get(championshipId);
			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
			if (!listChampionshipCategory.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
					ChampionshipCategoryDTO2 ChampionshipCategoryDTO2 = new ChampionshipCategoryDTO2(championshipCategory.getId(),
							championshipId,
							championshipCategory.getAsanaCategory().getName(),
							championshipCategory.getCategory().getTitle(), championshipCategory.getGender().toString());

					listChampionshipCategoryDTO.add(ChampionshipCategoryDTO2);
				}
			}
		} catch (ChampionshipNotFoundException e) {
			e.printStackTrace();
		}
		return listChampionshipCategoryDTO;
	}
	
	
	
}
