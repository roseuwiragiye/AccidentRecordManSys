package com.Webproject.AccidentRecordManSys.Service;

import com.Webproject.AccidentRecordManSys.Model.Accident;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccidentRecService {
    List<Accident> getAllAccidents();

    void saveAccident(Accident accident);
    Accident getAccidentById(long id);
    void deleteAccidentById(long id);
    Page<Accident> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
