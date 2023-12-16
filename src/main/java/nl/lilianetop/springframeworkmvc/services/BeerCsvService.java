package nl.lilianetop.springframeworkmvc.services;

import java.io.File;
import java.util.List;
import nl.lilianetop.springframeworkmvc.models.BeerCSVRecord;


public interface BeerCsvService {

  List<BeerCSVRecord> convertCSV(File csvFile);

}
