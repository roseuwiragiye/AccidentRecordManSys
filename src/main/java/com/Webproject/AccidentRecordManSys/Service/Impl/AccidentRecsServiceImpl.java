package com.Webproject.AccidentRecordManSys.Service.Impl;

import com.Webproject.AccidentRecordManSys.Model.Accident;
import com.Webproject.AccidentRecordManSys.Repository.AccidentRepository;
import com.Webproject.AccidentRecordManSys.Service.AccidentRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccidentRecsServiceImpl implements AccidentRecService {
    @Autowired
    private AccidentRepository accidentRepository;
    @Override
    public List<Accident> getAllAccidents() {
        return accidentRepository.findAll();
    }
    @Override
    public void saveAccident(Accident accident){
        this.accidentRepository.save(accident);
    }

    @Override
    public Accident getAccidentById(long id) {
        Optional<Accident> optional = accidentRepository.findById(id);
        Accident accident = null;
        if (optional.isPresent()){
            accident = optional.get();
        }else {
            throw new RuntimeException("Animal not found for id ::" +id);
        }
        return accident;
    }

    @Override
    public void deleteAccidentById(long id) {
        this.accidentRepository.deleteById(id);
    }

    @Override
    public Page<Accident> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.accidentRepository.findAll(pageable);
    }
}
