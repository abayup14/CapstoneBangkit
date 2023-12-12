SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `hAIre` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE IF NOT EXISTS `hAIre`.`user` (
  `iduser` INT(11) NOT NULL AUTO_INCREMENT,
  `nama` VARCHAR(100) NULL DEFAULT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `password` VARCHAR(128) NULL DEFAULT NULL,
  `nomor telepon` VARCHAR(12) NULL DEFAULT NULL,
  `tgl_lahir` DATETIME NULL DEFAULT NULL,
  `nik` VARCHAR(16) NULL DEFAULT NULL,
  `pengalaman` INT(11) NULL DEFAULT NULL,
  `pengalaman_pro` INT(11) NULL DEFAULT NULL,
  `edukasi` ENUM('Master', 'Undergraduate', 'PhD', 'Other', 'NoHigherEd') NULL DEFAULT NULL,
  `tdk_pnyrmh` TINYINT(4) NULL DEFAULT NULL,
  `url_photo` VARCHAR(1000) NULL DEFAULT NULL,
  `deskripsi` VARCHAR(1000) NULL DEFAULT NULL,
  `stream` ENUM('Pelatihan', 'Spesialisasi') NULL DEFAULT NULL,
  PRIMARY KEY (`iduser`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`company` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nama` VARCHAR(100) NULL DEFAULT NULL,
  `alamat` VARCHAR(100) NULL DEFAULT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `password` VARCHAR(100) NULL DEFAULT NULL,
  `url_photo` VARCHAR(1000) NULL DEFAULT NULL,
  `deskripsi` VARCHAR(1000) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`lowongan` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nama` VARCHAR(100) NULL DEFAULT NULL,
  `deskripsi` VARCHAR(1000) NULL DEFAULT NULL,
  `jmlh_butuh` INT(11) NULL DEFAULT NULL,
  `company_id` INT(11) NOT NULL,
  `url_photo` VARCHAR(1000) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_lowongan_company1_idx` (`company_id` ASC),
  CONSTRAINT `fk_lowongan_company1`
    FOREIGN KEY (`company_id`)
    REFERENCES `hAIre`.`company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`skills` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nama` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`user_has_skills` (
  `user_iduser` INT(11) NOT NULL,
  `skills_id` INT(11) NOT NULL,
  PRIMARY KEY (`user_iduser`, `skills_id`),
  INDEX `fk_user_has_skills_skills1_idx` (`skills_id` ASC),
  INDEX `fk_user_has_skills_user_idx` (`user_iduser` ASC),
  CONSTRAINT `fk_user_has_skills_user`
    FOREIGN KEY (`user_iduser`)
    REFERENCES `hAIre`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_skills_skills1`
    FOREIGN KEY (`skills_id`)
    REFERENCES `hAIre`.`skills` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`skills_dibutuhkan` (
  `skills_id` INT(11) NOT NULL,
  `lowongan_id` INT(11) NOT NULL,
  PRIMARY KEY (`skills_id`, `lowongan_id`),
  INDEX `fk_skills_has_lowongan_lowongan1_idx` (`lowongan_id` ASC),
  INDEX `fk_skills_has_lowongan_skills1_idx` (`skills_id` ASC),
  CONSTRAINT `fk_skills_has_lowongan_skills1`
    FOREIGN KEY (`skills_id`)
    REFERENCES `hAIre`.`skills` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_skills_has_lowongan_lowongan1`
    FOREIGN KEY (`lowongan_id`)
    REFERENCES `hAIre`.`lowongan` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`apply` (
  `user_iduser` INT(11) NOT NULL,
  `lowongan_id` INT(11) NOT NULL,
  `probabilitas` DOUBLE NULL DEFAULT NULL,
  `jaccard` DOUBLE NULL DEFAULT NULL,
  `skor_akhir` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`user_iduser`, `lowongan_id`),
  INDEX `fk_user_has_lowongan_lowongan1_idx` (`lowongan_id` ASC),
  INDEX `fk_user_has_lowongan_user1_idx` (`user_iduser` ASC),
  CONSTRAINT `fk_user_has_lowongan_user1`
    FOREIGN KEY (`user_iduser`)
    REFERENCES `hAIre`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_lowongan_lowongan1`
    FOREIGN KEY (`lowongan_id`)
    REFERENCES `hAIre`.`lowongan` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`notifikasi` (
  `idnotifikasi` INT(11) NOT NULL AUTO_INCREMENT,
  `waktu` DATETIME NULL DEFAULT NULL,
  `pesan` VARCHAR(100) NULL DEFAULT NULL,
  `user_iduser` INT(11) NOT NULL,
  PRIMARY KEY (`idnotifikasi`),
  INDEX `fk_notifikasi_user1_idx` (`user_iduser` ASC),
  CONSTRAINT `fk_notifikasi_user1`
    FOREIGN KEY (`user_iduser`)
    REFERENCES `hAIre`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`edukasi` (
  `id` INT(11) NOT NULL,
  `nama_institusi` VARCHAR(100) NULL DEFAULT NULL,
  `jenjang` ENUM('Master', 'Undergraduate', 'PhD', 'Other', 'NoHigherEd') NULL DEFAULT NULL,
  `tgl_awal` DATE NULL DEFAULT NULL,
  `tgl_akhir` DATE NULL DEFAULT NULL,
  `deskripsi` VARCHAR(1000) NULL DEFAULT NULL,
  `user_iduser` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_edukasi_user1_idx` (`user_iduser` ASC),
  CONSTRAINT `fk_edukasi_user1`
    FOREIGN KEY (`user_iduser`)
    REFERENCES `hAIre`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `hAIre`.`pengalaman` (
  `id` INT(11) NOT NULL,
  `nama_pekerjaan` VARCHAR(100) NULL DEFAULT NULL,
  `tgl_mulai` DATE NULL DEFAULT NULL,
  `tgl_selesai` DATE NULL DEFAULT NULL,
  `tmpt_pekerja` VARCHAR(100) NULL DEFAULT NULL,
  `pkrjn_profesional` TINYINT(4) NULL DEFAULT NULL,
  `user_iduser` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pengalaman_user1_idx` (`user_iduser` ASC),
  CONSTRAINT `fk_pengalaman_user1`
    FOREIGN KEY (`user_iduser`)
    REFERENCES `hAIre`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
