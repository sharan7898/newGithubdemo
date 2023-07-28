package com.swayaan.nysf.restcontroller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.ChampionshipLinkService;
import com.swayaan.nysf.utils.CommonUtil;

@RestController
public class ChampionshipLinkStatusRestController {

	@Autowired
	ChampionshipLinkService championshipLinkSerivce;
	@Autowired
	CommonUtil user;

	@PostMapping("/championshiplink/status")
	public String saveStatus(@RequestParam("status") Boolean status, @RequestParam("championshipLink_id") Integer Id) {
		try {
			ChampionshipLink championshipLink = championshipLinkSerivce.getById(Id);
			User currentUser = CommonUtil.getCurrentUser();

			if (championshipLink!=null) {
				championshipLink.setLastModifiedBy(currentUser);
				
				if (championshipLink.getStatus() == false) {
					championshipLink.setStatus(true);
					
					championshipLink.setLinkActivatedTime(new Date());
				} else {
					championshipLink.setStatus(false);	
					championshipLink.setLinkDeactivatedTime(new Date());
				}
				championshipLinkSerivce.saveChampionshipLink(championshipLink);
	}

			return "ok";

		} catch (Exception e) {
			return "fail";
		}

	}

}
