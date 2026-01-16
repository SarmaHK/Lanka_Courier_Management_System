package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.Rates;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RatesDAO {

    public void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS rates (" +
                "rate_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "min_weight_kg DECIMAL(10,2), " +
                "max_weight_kg DECIMAL(10,2), " +
                "price_lkr DECIMAL(10,2)" +
                ")";
        try (Connection conn = dbconnection.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            if (getCount() == 0)
                seedData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getCount() {
        try (Connection conn = dbconnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM rates")) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void seedData() {
        addRate(new Rates(0, new BigDecimal("0.00"), new BigDecimal("1.00"), new BigDecimal("350.00")));
        addRate(new Rates(0, new BigDecimal("1.01"), new BigDecimal("5.00"), new BigDecimal("550.00")));
        addRate(new Rates(0, new BigDecimal("5.01"), new BigDecimal("10.00"), new BigDecimal("850.00")));
        addRate(new Rates(0, new BigDecimal("10.01"), new BigDecimal("100.00"), new BigDecimal("1500.00")));
    }

    public boolean addRate(Rates r) {
        String sql = "INSERT INTO rates (min_weight_kg, max_weight_kg, price_lkr) VALUES (?,?,?)";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, r.getMinWeightKg());
            ps.setBigDecimal(2, r.getMaxWeightKg());
            ps.setBigDecimal(3, r.getPriceLkr());
            int affected = ps.executeUpdate();
            if (affected == 0)
                return false;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    r.setRateId(keys.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateRate(Rates r) {
        String sql = "UPDATE rates SET min_weight_kg=?, max_weight_kg=?, price_lkr=? WHERE rate_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, r.getMinWeightKg());
            ps.setBigDecimal(2, r.getMaxWeightKg());
            ps.setBigDecimal(3, r.getPriceLkr());
            ps.setInt(4, r.getRateId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteRate(int rateId) {
        String sql = "DELETE FROM rates WHERE rate_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rateId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Rates getRateById(int id) {
        String sql = "SELECT rate_id, min_weight_kg, max_weight_kg, price_lkr FROM rates WHERE rate_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRow(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Rates> getAllRates() {
        List<Rates> list = new ArrayList<>();
        String sql = "SELECT rate_id, min_weight_kg, max_weight_kg, price_lkr FROM rates ORDER BY min_weight_kg";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public Rates getRateForWeight(BigDecimal weightKg) {
        String sql = "SELECT rate_id, min_weight_kg, max_weight_kg, price_lkr FROM rates WHERE min_weight_kg <= ? AND max_weight_kg >= ? LIMIT 1";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, weightKg);
            ps.setBigDecimal(2, weightKg);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRow(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Rates mapRow(ResultSet rs) throws SQLException {
        Rates r = new Rates();
        r.setRateId(rs.getInt("rate_id"));
        r.setMinWeightKg(rs.getBigDecimal("min_weight_kg"));
        r.setMaxWeightKg(rs.getBigDecimal("max_weight_kg"));
        r.setPriceLkr(rs.getBigDecimal("price_lkr"));
        return r;
    }
}
