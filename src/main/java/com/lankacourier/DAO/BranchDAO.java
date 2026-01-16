package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.Branch;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO {

    // INSERT a new branch and set generated id back to branch object
    public boolean addBranch(Branch branch) {
        String sql = "INSERT INTO branches (b_name, b_type, district, province, postal_code, tel_no, latitude, longitude, branch_distance) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, branch.getName());
            ps.setString(2, branch.getType());
            ps.setString(3, branch.getDistrict());
            ps.setString(4, branch.getProvince());
            ps.setString(5, branch.getPostalCode());
            ps.setString(6, branch.getTelNo());
            ps.setDouble(7, branch.getLatitude() == null ? 0.0 : branch.getLatitude());
            ps.setDouble(8, branch.getLongitude() == null ? 0.0 : branch.getLongitude());
            ps.setDouble(9, branch.getBranchDistance() == null ? 0.0 : branch.getBranchDistance());

            int affected = ps.executeUpdate();
            if (affected == 0)
                return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    branch.setBranchId(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE branch
    public boolean updateBranch(Branch branch) {
        String sql = "UPDATE branches SET b_name=?, b_type=?, district=?, province=?, postal_code=?, tel_no=?, latitude=?, longitude=?, branch_distance=? "
                +
                "WHERE branch_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, branch.getName());
            ps.setString(2, branch.getType());
            ps.setString(3, branch.getDistrict());
            ps.setString(4, branch.getProvince());
            ps.setString(5, branch.getPostalCode());
            ps.setString(6, branch.getTelNo());
            ps.setDouble(7, branch.getLatitude() == null ? 0.0 : branch.getLatitude());
            ps.setDouble(8, branch.getLongitude() == null ? 0.0 : branch.getLongitude());
            ps.setDouble(9, branch.getBranchDistance() == null ? 0.0 : branch.getBranchDistance());
            ps.setInt(10, branch.getBranchId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE branch by ID
    public boolean deleteBranch(int branchId) {
        String sql = "DELETE FROM branches WHERE branch_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, branchId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET ALL branches
    public List<Branch> getAllBranches() {
        List<Branch> list = new ArrayList<>();
        String sql = "SELECT * FROM branches ORDER BY b_name";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // GET a single branch by ID
    public Branch getBranchById(int id) {
        String sql = "SELECT * FROM branches WHERE branch_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // optional: find by name
    public Branch getBranchByName(String name) {
        String sql = "SELECT * FROM branches WHERE b_name = ?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Branch map(ResultSet rs) throws SQLException {
        Branch b = new Branch(
                rs.getInt("branch_id"),
                rs.getString("b_name"),
                rs.getString("b_type"),
                rs.getString("district"),
                rs.getString("province"),
                rs.getString("postal_code"),
                rs.getString("tel_no"));
        try {
            b.setLatitude(rs.getDouble("latitude"));
            b.setLongitude(rs.getDouble("longitude"));
            b.setBranchDistance(rs.getDouble("branch_distance"));
        } catch (SQLException ignored) {
        }
        return b;
    }
}
