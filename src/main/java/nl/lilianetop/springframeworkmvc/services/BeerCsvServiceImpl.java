package nl.lilianetop.springframeworkmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import nl.lilianetop.springframeworkmvc.models.BeerCSVRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {

  @Override
  public List<BeerCSVRecord> convertCSV(File csvFile) {

    try {
      List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
          .withType(BeerCSVRecord.class)//maps it to this pojo
          .build().parse();
      return beerCSVRecords;
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
