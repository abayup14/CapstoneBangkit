'''from flask_sqlalchemy import SQLAlchemy
from flask import Flask'''
from app import db

# app = Flask(__name__)
# app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:mysql@localhost/hAIre'
# app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
# db = SQLAlchemy(app)


class User(db.Model):
    iduser = db.Column(db.Integer, primary_key=True)
    nama = db.Column(db.String(100))
    email = db.Column(db.String(100))
    password = db.Column(db.String(128))
    nomor_telepon = db.Column(db.String(12))
    tgl_lahir = db.Column(db.DateTime)
    nik = db.Column(db.String(16))
    pengalaman = db.Column(db.Integer)
    pengalaman_pro = db.Column(db.Integer)
    edukasi = db.Column(db.Enum('Master', 'Undergraduate', 'PhD', 'Other', 'NoHigherEd'))
    tdk_pnyrmh = db.Column(db.Boolean)
    url_photo = db.Column(db.String(1000))
    deskripsi = db.Column(db.String(1000))
    stream = db.Column(db.Enum('Pelatihan', 'Spesialisasi'))

    edukasis = db.relationship('Edukasi', backref='user', lazy=True)
    pengalamans = db.relationship('Pengalaman', backref='user', lazy=True)
    user_has_skills = db.relationship('UserHasSkills', backref='user', lazy=True)
    applies = db.relationship('Apply', backref='user', lazy=True)
    notifikasis = db.relationship('Notifikasi', backref='user', lazy=True)

    def to_dict(self):
        return {
            "iduser": self.iduser,
            "nama": self.nama,
            "email": self.email,
            "password": self.password,
            "nomor_telepon": self.nomor_telepon,
            "tgl_lahir": str(self.tgl_lahir),
            "nik": self.nik,
            "pengalaman": self.pengalaman,
            "pengalaman_pro": self.pengalaman_pro,
            "edukasi": self.edukasi,
            "tdk_pnyrmh": self.tdk_pnyrmh,
            "url_photo": self.url_photo,
            "deskripsi": self.deskripsi,
            "stream": self.stream
        }


class Company(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nama = db.Column(db.String(100))
    alamat = db.Column(db.String(100))
    email = db.Column(db.String(100))
    password = db.Column(db.String(100))
    url_photo = db.Column(db.String(1000))
    deskripsi = db.Column(db.String(1000))

    def to_dict(self):
        return {
            "id": self.id,
            "nama": self.nama,
            "alamat": self.alamat,
            "email": self.email,
            "password": self.password,
            "url_photo": self.url_photo,
            "deskripsi": self.deskripsi
        }


class Lowongan(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nama = db.Column(db.String(100))
    deskripsi = db.Column(db.String(1000))
    jmlh_butuh = db.Column(db.Integer)
    company_id = db.Column(db.Integer, db.ForeignKey('company.id'))
    url_photo = db.Column(db.String(1000))
    company = db.relationship('Company', backref=db.backref('lowongans', lazy=True))

    def to_dict(self):
        return {
            "id": self.id,
            "nama": self.nama,
            "deskripsi": self.deskripsi,
            "jmlh_butuh": self.jmlh_butuh,
            "company_id": self.company_id,
            "url_photo": self.url_photo
        }


class Skills(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nama = db.Column(db.String(100))

    def to_dict(self):
        return {
            "id": self.id,
            "nama": self.nama
        }


class UserHasSkills(db.Model):
    user_iduser = db.Column(db.Integer, db.ForeignKey('user.iduser'), primary_key=True)
    skills_id = db.Column(db.Integer, db.ForeignKey('skills.id'), primary_key=True)
    # user = db.relationship('User', backref=db.backref('user_has_skills', lazy=True))
    # skills = db.relationship('Skills', backref=db.backref('user_has_skills', lazy=True))


class SkillsDibutuhkan(db.Model):
    skills_id = db.Column(db.Integer, db.ForeignKey('skills.id'), primary_key=True)
    lowongan_id = db.Column(db.Integer, db.ForeignKey('lowongan.id'), primary_key=True)
    skills = db.relationship('Skills', backref=db.backref('skills_dibutuhkan', lazy=True))
    lowongan = db.relationship('Lowongan', backref=db.backref('skills_dibutuhkan', lazy=True))


class Apply(db.Model):
    user_iduser = db.Column(db.Integer, db.ForeignKey('user.iduser'), primary_key=True)
    lowongan_id = db.Column(db.Integer, db.ForeignKey('lowongan.id'), primary_key=True)
    probabilitas = db.Column(db.Float)
    jaccard = db.Column(db.Float)
    skor_akhir = db.Column(db.Float)
    # user = db.relationship('User', backref=db.backref('apply', lazy=True))
    # lowongan = db.relationship('Lowongan', backref=db.backref('apply', lazy=True))


class Notifikasi(db.Model):
    idnotifikasi = db.Column(db.Integer, primary_key=True)
    waktu = db.Column(db.DateTime)
    pesan = db.Column(db.String(100))
    user_iduser = db.Column(db.Integer, db.ForeignKey('user.iduser'))
    # user = db.relationship('User', backref=db.backref('notifikasi', lazy=True))


class Edukasi(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nama_institusi = db.Column(db.String(100))
    jenjang = db.Column(db.Enum('Master', 'Undergraduate', 'PhD', 'Other', 'NoHigherEd'))
    tgl_awal = db.Column(db.Date)
    tgl_akhir = db.Column(db.Date)
    deskripsi = db.Column(db.String(1000))
    user_iduser = db.Column(db.Integer, db.ForeignKey('user.iduser'))


class Pengalaman(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nama_pekerjaan = db.Column(db.String(100))
    tgl_mulai = db.Column(db.Date)
    tgl_selesai = db.Column(db.Date)
    tmpt_pekerja = db.Column(db.String(100))
    pkrjn_profesional = db.Column(db.Boolean)
    user_iduser = db.Column(db.Integer, db.ForeignKey('user.iduser'))
