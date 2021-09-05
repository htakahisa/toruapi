package htakahisa.service;

import htakahisa.domain.toru.ToruLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToruService {

    @Autowired
    private ToruLogic toruLogic;
}
