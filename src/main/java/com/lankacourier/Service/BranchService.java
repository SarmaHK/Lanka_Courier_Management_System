package com.lankacourier.Service;

import com.lankacourier.DAO.BranchDAO;
import com.lankacourier.Model.Branch;

import java.util.List;

public class BranchService {
    private final BranchDAO dao = new BranchDAO();

    public List<Branch> getAllBranches() {
        return dao.getAllBranches();
    }

    public boolean addBranch(Branch b) {
        // small business validation example:
        if (b.getName() == null || b.getName().isBlank()) return false;
        return dao.addBranch(b);
    }

    public boolean updateBranch(Branch b) {
        if (b.getBranchId() <= 0) return false;
        return dao.updateBranch(b);
    }

    public boolean deleteBranch(int id) {
        return dao.deleteBranch(id);
    }

    public Branch getById(int id) {
        return dao.getBranchById(id);
    }
}
