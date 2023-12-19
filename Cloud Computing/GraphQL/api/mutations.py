# mutations.py
from datetime import datetime
from ariadne import convert_kwargs_to_snake_case
from api.main import db
from api.models import User, Company, Lowongan, Skills, UserHasSkills, SkillsDibutuhkan, Apply, Notifikasi, Pengalaman, Edukasi


@convert_kwargs_to_snake_case
def create_user_resolver(obj, info, nama, email, password, nomor_telepon, tgl_lahir, nik, pengalaman, pengalaman_pro,
                         edukasi,tdk_pnyrmh,disabilitas, url_photo, deskripsi, stream):
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
            tdk_pnyrmh=tdk_pnyrmh,
            disabilitas=disabilitas,
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
def update_user_resolver(obj, info, iduser, pengalaman, pengalaman_pro):
    try:
        user = User.query.filter_by(iduser=iduser).first()
        if user:
            user.pengalaman += pengalaman
            user.pengalaman_pro += pengalaman_pro
            db.session.add(user)
            db.session.commit()
            payload = {
                "success": True,
                "user": user.to_dict()
            }
        else:
            payload = {
                "success": False,
                "errors": [f"item matching {iduser} not found"]
            }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"item matching {iduser} not found"]
        }
    return payload

@convert_kwargs_to_snake_case
def update_education_resolver(obj, info, iduser, edukasi):
    try:
        dict_ed = {
            "HighSchoolOrBelow": 1,
            "Other": 2,
            "Undergraduate": 3,
            "Master": 4,
            "PhD": 5
        }
        user = User.query.filter_by(iduser=iduser).first()
        if user:
            curr_ed = user.edukasi
            if dict_ed[edukasi] > dict_ed[curr_ed]:
                user.edukasi = edukasi
                db.session.add(user)
                db.session.commit()
                payload = {
                "success": True,
                "user": user.to_dict()
                }
            else:
                payload = {
                    "success": False,
                    "errors": [f"Inputted Education is lower than current education"]
                }
        else:
            payload = {
                "success": False,
                "errors": [f"item matching {iduser} not found"]
            }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"item matching {iduser} not found"]
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
            "skill": new_skills.to_dict()
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

@convert_kwargs_to_snake_case
def create_notifikasi_resolver(obj, info, waktu, pesan, user_iduser):
    try:
        new_notifikasi = Notifikasi(waktu=waktu,pesan=pesan,user_iduser=user_iduser)
        db.session.add(new_notifikasi)
        db.session.commit()
        payload = {
            "success": True,
            "notifikasi": new_notifikasi.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


# diubah vincent
@convert_kwargs_to_snake_case
def create_apply_resolver(obj, info, user_iduser, lowongan_id, probabilitas, jaccard, skor_akhir, status):
    try:
        new_apply = Apply(user_iduser=user_iduser, lowongan_id=lowongan_id, probabilitas=probabilitas, jaccard=jaccard, skor_akhir=skor_akhir,status=status)
        db.session.add(new_apply)
        db.session.commit()
        payload = {
            "success": True,
            "apply": new_apply.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def update_user_apply_status_resolver(obj, info, user_iduser, lowongan_id, status):
    try:
        apply_update = Apply.query.filter_by(user_iduser=user_iduser, lowongan_id=lowongan_id).first()
        if apply_update:
            apply_update.status = status
            db.session.add(apply_update)
            db.session.commit()
            payload = {
                "success": True,
                "apply": apply_update.to_dict()
            }
        else:
            payload = {
                "success": False,
                "errors": [f"item matching {apply_update} not found"]
            }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"item matching {apply_update} not found"]
        }
    return payload

@convert_kwargs_to_snake_case
def update_user_description_resolver(obj, info, iduser, deskripsi):
    try:
        user_update = User.query.filter_by(iduser=iduser).first()
        if user_update:
            user_update.deskripsi = deskripsi
            db.session.add(user_update)
            db.session.commit()
            payload = {
                "success": True,
                "user": user_update.to_dict()
            }
        else:
            payload = {
                "success": False,
                "errors": [f"item matching {user_update} not found"]
            }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"item matching {user_update} not found"]
        }
    return payload

@convert_kwargs_to_snake_case
def update_user_url_photo_resolver(obj, info, iduser, url_photo):
    try:
        user_update = User.query.filter_by(iduser=iduser).first()
        if user_update:
            user_update.url_photo = url_photo
            db.session.add(user_update)
            db.session.commit()
            payload = {
                "success": True,
                "user": user_update.to_dict()
            }
        else:
            payload = {
                "success": False,
                "errors": [f"item matching {user_update} not found"]
            }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"item matching {user_update} not found"]
        }
    return payload

@convert_kwargs_to_snake_case
def update_company_url_photo_resolver(obj, info, id, url_photo):
    try:
        company_update = Company.query.filter_by(id=id).first()
        if company_update:
            company_update.url_photo = url_photo
            db.session.add(company_update)
            db.session.commit()
            payload = {
                "success": True,
                "company": company_update.to_dict()
            }
        else:
            payload = {
                "success": False,
                "errors": [f"item matching {company_update} not found"]
            }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"item matching {company_update} not found"]
        }
    return payload
