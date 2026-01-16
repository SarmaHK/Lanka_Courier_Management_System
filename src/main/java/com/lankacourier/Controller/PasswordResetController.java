package com.lankacourier.Controller;

import com.lankacourier.DAO.PasswordResetDAO;
import com.lankacourier.Model.PasswordResetRequest;
import java.util.List;

public class PasswordResetController {

    private final PasswordResetDAO dao = new PasswordResetDAO();

    public boolean requestReset(String username) {
        return dao.createRequest(username);
    }

    public boolean approveRequest(int requestId) {
        return dao.updateStatus(requestId, "APPROVED");
    }

    public boolean completeRequest(String username) {
        return dao.completeRequest(username);
    }

    public PasswordResetRequest getStatus(String username) {
        return dao.findPendingOrApproved(username);
    }

    public List<PasswordResetRequest> getPendingRequests() {
        return dao.fetchAllPending();
    }
}
