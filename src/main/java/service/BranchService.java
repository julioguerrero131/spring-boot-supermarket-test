package service;

import dto.BranchDTO;
import exception.NotFoundException;
import maper.Mapper;
import model.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BranchRepository;

import java.util.List;

@Service
public class BranchService implements IBranchService {
    @Autowired
    private BranchRepository repo;

    @Override
    public List<BranchDTO> getBranches() {
        return repo.findAll()
                .stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public BranchDTO createBranch(BranchDTO branchDto) {
        Branch b = Branch.builder()
                .name(branchDto.getName())
                .address(branchDto.getAddress())
                .build();

        return Mapper.toDTO(repo.save(b));
    }

    @Override
    public BranchDTO updateBranch(Long id, BranchDTO branchDto) {
        Branch b = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found!"));

        b.setName(branchDto.getName());
        b.setAddress(branchDto.getAddress());

        return Mapper.toDTO(repo.save(b));
    }

    @Override
    public void deleteBranch(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Branch not found!");
        }

        repo.deleteById(id);
    }
}
