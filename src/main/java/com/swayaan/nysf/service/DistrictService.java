package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.District;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.DTO.DistrictDTO;
import com.swayaan.nysf.exception.DistrictNotFoundException;
import com.swayaan.nysf.repository.DistrictRepository;

@Service
public class DistrictService {

	@Autowired
	DistrictRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(DistrictService.class);

	public List<District> listAllDistrict() {
		LOGGER.info("Entered listAllDistrict method -DistrictService");
		LOGGER.info("Exit listAllDistrict method -DistrictService");
		return repo.findAllByOrderByNameAsc();
	}

	public District get(Integer id) throws DistrictNotFoundException {
		LOGGER.info("Entered get method -DistrictService");
		try {
			LOGGER.info("Exit get method -DistrictService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new DistrictNotFoundException("Could not find any state with ID " + id);
		}
	}

	public List<District> findByStateOrderByNameAsc(State state) {
		LOGGER.info("Entered findByStateOrderByNameAsc method -DistrictService");
		LOGGER.info("Exit findByStateOrderByNameAsc method -DistrictService");
		return repo.findByStateOrderByNameAsc(state);
	}

	// Conversions starts here
	public List<DistrictDTO> convertDistrictToDistrictDto(List<District> listDistrict) {
		LOGGER.info("Entered convertDistrictToDistrictDto method -DistrictService");
		List<DistrictDTO> listDistrictsDTO = new ArrayList<>();
		for (District district : listDistrict) {
			DistrictDTO districtDto = new DistrictDTO();
			districtDto.setId(district.getId());
			districtDto.setName(district.getName());
			listDistrictsDTO.add(districtDto);
		}
		LOGGER.info("Exit convertDistrictToDistrictDto method -DistrictService");
		return listDistrictsDTO;
	}

	public boolean isExistsByName(String districtName) throws DistrictNotFoundException {
		LOGGER.info("Entered isExistsByName method -DistrictService");
		try {
			LOGGER.info("Exit isExistsByName method -DistrictService");
			return repo.existsByName(districtName);
		} catch (NoSuchElementException ex) {
			throw new DistrictNotFoundException("Could not find any district with Name " + districtName);
		}
	}

	public District findByStateAndName(State state, String district) throws DistrictNotFoundException {
		LOGGER.info("Entered findByStateAndName method -DistrictService");
		try {
			LOGGER.info("Exit findByStateAndName method -DistrictService");
			System.out.println(district);
			return repo.findByStateAndName(state, district);
		} catch (NoSuchElementException ex) {
			throw new DistrictNotFoundException("Could not find any district with Name " + district);
		}
	}

}
