package controller;

import dto.BranchDTO;
import model.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IBranchService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {
    @Autowired
    private IBranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchDTO>> getBranches() {
        return ResponseEntity.ok(branchService.getBranches());
    }

    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO branchDTO) {
        BranchDTO b = branchService.createBranch(branchDTO);
        return ResponseEntity.created(URI.create("/api/branches/" + b.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> updateBranch(
            @PathVariable Long id,
            @RequestBody BranchDTO branchDTO
    ) {
        return ResponseEntity.ok(branchService.updateBranch(id, branchDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}
