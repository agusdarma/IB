ALTER TABLE distribution_detail ADD nama VARCHAR(100)
ALTER TABLE distribution_detail ADD npwp VARCHAR(100)
ALTER TABLE distribution_detail ADD alamat VARCHAR(100)
ALTER TABLE distribution_detail ADD jenis_pajak VARCHAR(100)
ALTER TABLE distribution_detail ADD jenis_setoran VARCHAR(100)
ALTER TABLE distribution_detail ADD start_date_pbb DATETIME
ALTER TABLE distribution_detail ADD end_date_pbb DATETIME
ALTER TABLE distribution_detail ADD tahun_pajak VARCHAR(100)
ALTER TABLE distribution_detail ADD jumlah_setor DECIMAL
ALTER TABLE distribution_detail MODIFY COLUMN phone_no VARCHAR(50)
ALTER TABLE distribution_detail MODIFY COLUMN account_no VARCHAR(50)
ALTER TABLE distribution_detail MODIFY COLUMN account_name VARCHAR(100)
ALTER TABLE distribution_detail MODIFY COLUMN money_value DECIMAL
ALTER TABLE distribution_detail ADD npwp_penyetor VARCHAR(100)
ALTER TABLE distribution_detail ADD uraian_ssp VARCHAR(100)
ALTER TABLE distribution_detail ADD no_sk VARCHAR(100)
ALTER TABLE distribution_detail ADD kode_kkp_ssp VARCHAR(100)
ALTER TABLE distribution_detail ADD id_billing VARCHAR(100)
ALTER TABLE distribution_detail ADD masa_aktif_billing VARCHAR(100)

ALTER TABLE distribution_header ADD srac_name VARCHAR(100)
ALTER TABLE distribution_header ADD trx_code VARCHAR(100)

ALTER TABLE distribution_detail ADD tanggal_jam_bayar VARCHAR(100)

