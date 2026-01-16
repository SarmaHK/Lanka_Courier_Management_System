package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.Parcel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParcelDAO {

    public boolean addParcel(Parcel p) {
        String sql = "INSERT INTO parcels (tracking_id, sender_id, receiver_id, origin_branch_id, destination_branch_id, description, weight_kg, distance_km, price_lkr, status, created_at, is_cod, cod_amount_lkr, service_charge_lkr, mo_commission_lkr, cod_payment_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getTrackingId());
            ps.setInt(2, p.getSenderId());
            ps.setInt(3, p.getReceiverId());
            ps.setInt(4, p.getOriginBranchId());
            if (p.getDestinationBranchId() == null)
                ps.setNull(5, Types.INTEGER);
            else
                ps.setInt(5, p.getDestinationBranchId());
            ps.setString(6, p.getDescription());
            ps.setBigDecimal(7, p.getWeightKg());
            ps.setBigDecimal(8, p.getDistanceKm());
            ps.setBigDecimal(9, p.getPriceLkr());
            ps.setString(10, p.getStatus());
            if (p.getCreatedAt() == null)
                ps.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            else
                ps.setTimestamp(11, Timestamp.valueOf(p.getCreatedAt()));
            ps.setBoolean(12, p.isCod());
            if (p.getCodAmountLkr() == null)
                ps.setNull(13, Types.DECIMAL);
            else
                ps.setBigDecimal(13, p.getCodAmountLkr());
            if (p.getServiceChargeLkr() == null)
                ps.setNull(14, Types.DECIMAL);
            else
                ps.setBigDecimal(14, p.getServiceChargeLkr());
            if (p.getMoCommissionLkr() == null)
                ps.setNull(15, Types.DECIMAL);
            else
                ps.setBigDecimal(15, p.getMoCommissionLkr());
            ps.setString(16, p.getCodPaymentStatus());

            int affected = ps.executeUpdate();
            if (affected == 0)
                return false;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    p.setParcelId(keys.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateParcel(Parcel p) {
        String sql = "UPDATE parcels SET tracking_id=?, sender_id=?, receiver_id=?, origin_branch_id=?, destination_branch_id=?, description=?, weight_kg=?, distance_km=?, price_lkr=?, status=?, is_cod=?, cod_amount_lkr=?, service_charge_lkr=?, mo_commission_lkr=?, cod_payment_status=? WHERE parcel_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTrackingId());
            ps.setInt(2, p.getSenderId());
            ps.setInt(3, p.getReceiverId());
            ps.setInt(4, p.getOriginBranchId());
            if (p.getDestinationBranchId() == null)
                ps.setNull(5, Types.INTEGER);
            else
                ps.setInt(5, p.getDestinationBranchId());
            ps.setString(6, p.getDescription());
            ps.setBigDecimal(7, p.getWeightKg());
            ps.setBigDecimal(8, p.getDistanceKm());
            ps.setBigDecimal(9, p.getPriceLkr());
            ps.setString(10, p.getStatus());
            ps.setBoolean(11, p.isCod());
            if (p.getCodAmountLkr() == null)
                ps.setNull(12, Types.DECIMAL);
            else
                ps.setBigDecimal(12, p.getCodAmountLkr());
            if (p.getServiceChargeLkr() == null)
                ps.setNull(13, Types.DECIMAL);
            else
                ps.setBigDecimal(13, p.getServiceChargeLkr());
            if (p.getMoCommissionLkr() == null)
                ps.setNull(14, Types.DECIMAL);
            else
                ps.setBigDecimal(14, p.getMoCommissionLkr());
            ps.setString(15, p.getCodPaymentStatus());
            ps.setInt(16, p.getParcelId());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteParcel(int parcelId) {
        String sql = "DELETE FROM parcels WHERE parcel_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parcelId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Parcel getParcelById(int id) {
        String sql = "SELECT * FROM parcels WHERE parcel_id=?";
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

    public Parcel getByTrackingId(String trackingId) {
        String sql = "SELECT * FROM parcels WHERE tracking_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRow(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getNextParcelId() {
        String sql = "SELECT MAX(parcel_id) FROM parcels";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // If table is empty, max is null/0, so return 1.
                // rs.getInt returns 0 if null, so 0+1 = 1.
                return rs.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public List<Parcel> getAllParcels() {
        List<Parcel> list = new ArrayList<>();
        String sql = "SELECT * FROM parcels ORDER BY created_at DESC";
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

    private Parcel mapRow(ResultSet rs) throws SQLException {
        Integer destBranch = rs.getObject("destination_branch_id") == null ? null : rs.getInt("destination_branch_id");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = ts == null ? null : ts.toLocalDateTime();

        Parcel p = new Parcel();
        p.setParcelId(rs.getInt("parcel_id"));
        p.setTrackingId(rs.getString("tracking_id"));
        p.setSenderId(rs.getInt("sender_id"));
        p.setReceiverId(rs.getInt("receiver_id"));
        p.setOriginBranchId(rs.getInt("origin_branch_id"));
        p.setDestinationBranchId(destBranch);
        p.setDescription(rs.getString("description"));
        p.setWeightKg(rs.getBigDecimal("weight_kg"));
        p.setDistanceKm(rs.getBigDecimal("distance_km"));
        p.setPriceLkr(rs.getBigDecimal("price_lkr"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(createdAt);
        p.setCod(rs.getBoolean("is_cod"));
        p.setCodAmountLkr(rs.getBigDecimal("cod_amount_lkr"));
        p.setServiceChargeLkr(rs.getBigDecimal("service_charge_lkr"));
        p.setMoCommissionLkr(rs.getBigDecimal("mo_commission_lkr"));
        p.setCodPaymentStatus(rs.getString("cod_payment_status"));
        return p;
    }
}
