package com.lankacourier.Controller;

import com.lankacourier.Model.Branch;
import com.lankacourier.Service.BranchService;

import java.util.List;

public class BranchController {
    private final BranchService service = new BranchService();

    public List<Branch> fetchAll() {
        return service.getAllBranches();
    }

    public boolean create(Branch b) {
        return service.addBranch(b);
    }

    public boolean update(Branch b) {
        return service.updateBranch(b);
    }

    public boolean delete(int id) {
        return service.deleteBranch(id);
    }

    public Branch findById(int id) {
        return service.getById(id);
    }
}
