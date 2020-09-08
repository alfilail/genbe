package com.genbe.anothertries.service;

import com.genbe.anothertries.dto.*;
import com.genbe.anothertries.entity.*;

public interface DataService {
	DataLengkapDto getAge(DataLengkapDto dlDto, DataDto dataDto);
	DataLengkapDto getAge1(DataLengkapDto dlDto);
	//Person saveData(Person person);
}
