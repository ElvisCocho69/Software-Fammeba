package com.api.server.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.api.server.dto.client.SaveClientDTO;
import com.api.server.dto.client.ShowClientDTO;
import com.api.server.service.client.ClientService;
import com.api.server.util.ClientPdfExporter;
import com.api.server.util.ClientExcelExporter;
import com.api.server.util.ClientCsvExporter;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ShowClientDTO>> listAllClients(
        @RequestParam(required = false) String clienttype,
        @RequestParam(required = false) String status,
        Pageable pageable
    ) {
        try {
            Page<ShowClientDTO> clients = clientService.findAll(clienttype, status, pageable);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPDF(
            @RequestParam(required = false) String clienttype,
            @RequestParam(required = false) String status) {
        try {
            Page<ShowClientDTO> clientsPage = clientService.findAll(clienttype, status, Pageable.unpaged());
            List<ShowClientDTO> clients = clientsPage.getContent();
            
            ClientPdfExporter exporter = new ClientPdfExporter();
            byte[] pdfBytes = exporter.export(clients);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "clientes.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) String clienttype,
            @RequestParam(required = false) String status) {
        try {
            Page<ShowClientDTO> clientsPage = clientService.findAll(clienttype, status, Pageable.unpaged());
            List<ShowClientDTO> clients = clientsPage.getContent();
            
            ClientExcelExporter exporter = new ClientExcelExporter();
            byte[] excelBytes = exporter.export(clients);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("filename", "clientes.xlsx");
            
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportToCSV(
            @RequestParam(required = false) String clienttype,
            @RequestParam(required = false) String status) {
        try {
            Page<ShowClientDTO> clientsPage = clientService.findAll(clienttype, status, Pageable.unpaged());
            List<ShowClientDTO> clients = clientsPage.getContent();
            
            ClientCsvExporter exporter = new ClientCsvExporter();
            byte[] csvBytes = exporter.export(clients);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("filename", "clientes.csv");
            
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowClientDTO> findClientById(@PathVariable Long id) {
        ShowClientDTO client = clientService.findClientById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<ShowClientDTO> saveClient(@RequestBody SaveClientDTO clientDTO) {
        ShowClientDTO client = clientService.saveClient(clientDTO);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowClientDTO> updateClient(@PathVariable Long id, @RequestBody SaveClientDTO clientDTO) {
        ShowClientDTO client = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<ShowClientDTO> disableClient(@PathVariable Long id) {
        ShowClientDTO client = clientService.disableClient(id);
        return ResponseEntity.ok(client);
    }
}
