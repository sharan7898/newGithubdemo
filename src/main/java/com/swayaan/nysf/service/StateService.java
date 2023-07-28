package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.exception.StateNotFoundException;
import com.swayaan.nysf.repository.StateRepository;

@Service
public class StateService {

	@Autowired
	StateRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(StateService.class);

	public List<State> listAllStates() {
		LOGGER.info("Entered listAllStates method -StateService");
		LOGGER.info("Exit listAllStates method -StateService");
		return repo.findAllByOrderByNameAsc();
	}

	public State get(Integer id) throws StateNotFoundException {
		LOGGER.info("Entered get method -StateService");
		try {
			LOGGER.info("Exit get method -StateService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new StateNotFoundException("Could not find any state with ID " + id);
		}
	}

	public boolean isExistsByName(String stateName) throws StateNotFoundException {
		LOGGER.info("Entered isExistsByName method -StateService");
		try {
			LOGGER.info("Exit isExistsByName method -StateService");
			return repo.existsByName(stateName);
		} catch (NoSuchElementException ex) {
			throw new StateNotFoundException("Could not find any state with Name " + stateName);
		}
	}

	public State findByName(String state) throws StateNotFoundException {
		LOGGER.info("Entered findByName method -StateService");
		try {
			LOGGER.info("Exit findByName method -StateService");
			return repo.findByName(state);
		} catch (NoSuchElementException ex) {
			throw new StateNotFoundException("Could not find any state with Name " + state);
		
		}}
}
