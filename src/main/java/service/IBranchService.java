package service;

import dto.BranchDTO;

import java.util.List;

public interface IBranchService {

    List<BranchDTO> getBranches();
    BranchDTO createBranch(BranchDTO branchDto);
    BranchDTO updateBranch(Long id, BranchDTO branchDto);
    void deleteBranch(Long id);

}
