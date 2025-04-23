package dev.dixie.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApplicationController {

    @GetMapping("/find")
    @Operation(summary = "find the smallest n-number")
    public ResponseEntity<Integer> findNSmallestNumber(@RequestParam String path, @RequestParam int n) throws IOException {
        List<Integer> numbers = new ArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                numbers.add((int) cell.getNumericCellValue());
            }
        }

        int[] array = numbers.stream().mapToInt(Integer::intValue).toArray();

        if (n > array.length) {
            return new ResponseEntity<>(-1, HttpStatus.BAD_REQUEST);
        }

        for (int i = 0; i < n; i++) {
            int minNumIndex = i;

            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minNumIndex]) {
                    minNumIndex = j;
                }
            }

            int temporary = array[i];
            array[i] = array[minNumIndex];
            array[minNumIndex] = temporary;
        }

        return new ResponseEntity<>(array[n - 1], HttpStatus.OK);
    }
}
