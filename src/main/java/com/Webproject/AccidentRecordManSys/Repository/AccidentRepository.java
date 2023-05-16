package com.Webproject.AccidentRecordManSys.Repository;

import com.Webproject.AccidentRecordManSys.Model.Accident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccidentRepository extends JpaRepository<Accident, Long> {
}
