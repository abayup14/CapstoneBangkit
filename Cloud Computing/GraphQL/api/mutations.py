# mutations.py
from datetime import datetime
from ariadne import convert_kwargs_to_snake_case
from api.main import db
from api.models import User, Company, Lowongan, Skills, UserHasSkills, SkillsDibutuhkan, Apply, Notifikasi, Pengalaman, Edukasi


@convert_kwargs_to_snake_case
def create_user_resolver(obj, info, nama, email, password, nomor_telepon, tgl_lahir, nik, pengalaman, pengalaman_pro,
                         edukasi, url_photo, deskripsi, stream):
    try:
        new_user = User(
            nama=nama,
            email=email,
            password=password,
            nomor_telepon=nomor_telepon,
            tgl_lahir=tgl_lahir,
            nik=nik,
            pengalaman=pengalaman,
            pengalaman_pro=pengalaman_pro,
            edukasi=edukasi,
            url_photo=url_photo,
            deskripsi=deskripsi,
            stream=stream
        )
        #user_data = input.get('user')
        #user = User(**user_data)
        db.session.add(new_user)
        db.session.commit()
        payload = {
            "success": True,
            "user": new_user.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_company_resolver(obj, info, nama, alamat, email, password, url_photo, deskripsi):
    try:
        new_company = Company(
            nama=nama,
            alamat=alamat,
            email=email,
            password=password,
            url_photo=url_photo,
            deskripsi=deskripsi
        )
        #company_data = input.get('company')
        #company = Company(**company_data)
        db.session.add(new_company)
        db.session.commit()
        payload = {
            "success": True,
            "company": new_company.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_lowongan_resolver(obj, info, nama, deskripsi, jmlh_butuh, company_id, url_photo):
    try:
        new_lowongan = Lowongan(
            nama=nama,
            deskripsi=deskripsi,
            jmlh_butuh=jmlh_butuh,
            company_id=company_id,
            url_photo=url_photo
        )
        #lowongan_data = input.get('lowongan')
        #lowongan = Lowongan(**lowongan_data)
        db.session.add(new_lowongan)
        db.session.commit()
        payload = {
            "success": True,
            "lowongan": new_lowongan.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_skills_resolver(obj, info, nama):
    try:
        new_skills = Skills(nama=nama)
        #skills_data = input.get('skills')
        #skills = Skills(**skills_data)
        db.session.add(new_skills)
        db.session.commit()
        payload = {
            "success": True,
            "skills": new_skills.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def create_skills_required_resolver(obj, info, skills_id, lowongan_id):
    try:
        new_skills_req = SkillsDibutuhkan(skills_id=skills_id, lowongan_id=lowongan_id)
        db.session.add(new_skills_req)
        db.session.commit()
        payload = {
            "success": True,
            "skills": new_skills_req.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_user_has_skills_resolver(obj, info, user_iduser, skills_id):
    try:
        new_uhs = UserHasSkills(user_iduser=user_iduser, skills_id=skills_id)
        db.session.add(new_uhs)
        db.session.commit()
        payload = {
            "success": True,
            "user_has_skills": new_uhs.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def create_pengalaman_resolver(obj, info, nama_pekerjaan, tgl_mulai, tgl_selesai, tmpt_bekerja, pkrjn_profesional, user_iduser):
    try:
        new_pengalaman = Pengalaman(nama_pekerjaan=nama_pekerjaan, tgl_mulai=tgl_mulai, tgl_selesai=tgl_selesai, tmpt_pekerja=tmpt_bekerja, pkrjn_profesional=pkrjn_profesional, user_iduser=user_iduser)
        db.session.add(new_pengalaman)
        db.session.commit()
        payload = {
            "success": True,
            "pengalaman": new_pengalaman.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def create_edukasi_resolver(obj, info, nama_institusi, jenjang, tgl_awal, tgl_akhir, deskripsi, user_iduser):
    try:
        new_edukasi = Edukasi(nama_institusi=nama_institusi, jenjang=jenjang, tgl_awal=tgl_awal, tgl_akhir=tgl_akhir, deskripsi=deskripsi, user_iduser=user_iduser)
        db.session.add(new_edukasi)
        db.session.commit()
        payload = {
            "success": True,
            "edukasi": new_edukasi.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

