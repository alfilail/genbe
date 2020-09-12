var tableBiodata = {
    create: function () {
        // jika table tersebut datatable, maka clear and dostroy
        if ($.fn.DataTable.isDataTable('#tableBiodata')) {
            //table yg sudah dibentuk menjadi datatable harus d rebuild lagi untuk di instantiasi ulang
            $('#tableBiodata').DataTable().clear();
            $('#tableBiodata').DataTable().destroy();
        }

        $.ajax({
            url: '/biodata/data',
            method: 'get',
            contentType: 'application/json',
            success: function (res, status, xhr) {
                if (xhr.status == 200 || xhr.status == 201) {
                    $('#tableBiodata').DataTable({
                        data: res,
                        columns: [
                            {
                                title: "ID Number",
                                data: "nik"
                            },
                            {
                                title: "Name",
                                data: "name"
                            },
                            {
                                title: "Address",
                                data: "address"
                            },
                            {
                                title: "Mobile",
                                data: "hp"
                            },
                            {
                                title: "Birthdate",
                                data: "tgl"
                            },
                            {
                                title: "Birthplace",
                                data: "tempatLahir"
                            },
                            {
                                title: "Action",
                                data: null,
                                render: function (data, type, row) {
                                    return "<button class='btn-primary' onclick=formBiodata.setEditData('" + data.id + "')>Edit</button>"
                                }
                            },
                            {
                                title: "Action",
                                data: null,
                                render: function (data, type, row) {
                                    return "<button class='btn-primary' onclick=formBiodata.setDeleteData('" + data.id + "')>Delete</button>"
                                }
                            }
                        ]
                    });

                } else {

                }
            },
            error: function (err) {
                console.log(err);
            }
        });


    },
    creatependidikan: function (idperson) {
        // jika table tersebut datatable, maka clear and dostroy
        if ($.fn.DataTable.isDataTable('#tableBiodata')) {
            //table yg sudah dibentuk menjadi datatable harus d rebuild lagi untuk di instantiasi ulang
            $('#tableBiodata').DataTable().clear();
            $('#tableBiodata').DataTable().destroy();
        }

        $('#tableBiodata').DataTable({
            data: datapendidikan,
            columns: [
                {
                    title: "Jenjang Pendidikan",
                    data: "jenjang"
                },
                {
                    title: "Institusi",
                    data: "institusi"
                },
                {
                    title: "Tahun Masuk",
                    data: "masuk"
                },
                {
                    title: "Tahun Lulus",
                    data: "lulus"
                },
                {
                    title: "Action",
                    data: null,
                    render: function (data, type, full, meta) {
                        return "<button class='btn-primary' onclick=formBiodata.setEditDataPendidikan('" + meta.row + "')>Edit</button>"
                    }
                },
                {
                    title: "Action",
                    data: null,
                    render: function (data, type, full, meta) {
                        return "<button class='btn-primary' onclick=formBiodata.setDeletePendidikan('" + meta.row + "')>Delete</button>"
                    }
                }
            ]
        });
    }
};

var formBiodata = {
    resetForm: function () {
        $('#form-biodata')[0].reset();
    },
    saveForm: function () {
        if ($('#form-biodata').parsley().validate()) {
            var dataResult = getJsonForm($("#form-biodata").serializeArray(), true);
            $.ajax({
                url: '/biodata',
                method: 'post',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(dataResult),
                success: function (output) {
                    if (output.status == "true") {
                        tableBiodata.create();
                        $('#modal-biodata').modal('hide')
                        Swal.fire({
                            position: 'center-middle',
                            icon: 'success',
                            title: 'Your data has been saved',
                            showConfirmButton: false,
                            timer: 1000
                        })
                    } else if (output.message == "data gagal masuk, jumlah digit nik tidak sama dengan 16") {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Your ID Number must be 16 digits',
                            footer: '<h6>Please input the correct format</h6>'
                        })
                    } else if (output.message == "data gagal masuk, umur kurang dari 30 tahun") {
                        Swal.fire({
                            title: 'Hi there..',
                            text: 'Cute baby, you must be at least 30',
                            imageUrl: 'https://static.boredpanda.com/blog/wp-content/uploads/2019/07/baby-portraits-teeth-added-coffee-creek-studio-amy-haehl-2-5d3e904f5e700__880.jpg',
                            imageWidth: 500,
                            imageHeight: 300,
                            imageAlt: 'Smiling baby',
                        })
                    } else if (output.message == "data gagal masuk, jumlah digit nik tidak sama dengan 16 dan umur kurang dari 30 tahun") {
                        Swal.fire({
                            icon: 'warning',
                            title: 'Wrong Format',
                            text: 'ID Number must be 16 digits and make sure you are already 30'
                        })
                    } else if (output.message == "nik sudah ada") {
                        Swal.fire({
                            icon: 'info',
                            title: 'For Your Information',
                            text: 'ID Number "' + $('#nik').val() + '" already exists',
                            footer: '<h6>Kindly check it out at the "Get the Data!" toolbar</h6>'
                        })
                    }
                    else {

                    }
                },
                erorrr: function (err) {
                    console.log(err);
                }
            });
        }

    },
    saveFormEdit: function () {
        var dataResult = getJsonForm($("#form-biodata").serializeArray(), true);
        $.ajax({
            url: '/biodata/editbio',
            method: 'post',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(dataResult),
            success: function (res, status, xhr) {
                if (xhr.status == 200 || xhr.status == 201) {
                    tableBiodata.create();
                    $('#modal-biodata').modal('hide')
                    editbio = 0;
                } else {

                }
            },
            erorrr: function (err) {
                console.log(err);
            }
        });

    },
    saveFormPend: function (idperson) {
        var dataResult = getJsonForm($("#form-biodata").serializeArray(), true);
        datapendidikan.push(dataResult);
        $('#modal-biodata').modal('hide');
        tableBiodata.creatependidikan(idperson);
    },
    setEditDataPendidikan: function (row) {
        $('#modal-biodata').fromJSON(JSON.stringify(datapendidikan[row]));
        $('#modal-biodata').modal('show');
        newrow = row;
    },
    saveFormPendBaru: function (idperson) {
        var resultbaru = getJsonForm($("#form-biodata").serializeArray(), true);
        datapendidikan[newrow] = resultbaru;
        $('#modal-biodata').modal('hide');
        tableBiodata.creatependidikan(idperson);
        newrow = -1;
    },
    submitPend: function (idPerson) {
        $.ajax({
            url: '/biodata/' + idPerson,
            method: 'post',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(datapendidikan),
            success: function (res, status, xhr) {
                datapendidikan = [];
                tableBiodata.creatependidikan(idPerson);
            },
            erorrr: function (err) {
                console.log(err);
            }
        });
    },
    setDeletePendidikan: function (row) {
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire(
                    'Deleted!',
                    'Your file has been deleted.',
                    'success'
                )
                $('#tableBiodata').DataTable().row(row).remove().draw();
                datapendidikan.splice(row, 1);
            }
        })
    },
    getbynik: function (nik) {
        if ($.fn.DataTable.isDataTable('#tableBiodata')) {
            //table yg sudah dibentuk menjadi datatable harus d rebuild lagi untuk di instantiasi ulang
            $('#tableBiodata').DataTable().clear();
            $('#tableBiodata').DataTable().destroy();
        }
        $.ajax({
            url: '/biodata/bynik/' + nik,
            method: 'get',
            contentType: 'application/json',
            dataType: 'json',

            success: function (result) {
                if (result[0].status == 'true') {
                    $('#tableBiodata').DataTable({
                        data: [result[0].data],
                        columns: [
                            {
                                title: "ID Number",
                                data: "nik"
                            },
                            {
                                title: "Name",
                                data: "name"
                            },
                            {
                                title: "Address",
                                data: "address"
                            },
                            {
                                title: "Mobile",
                                data: "hp"
                            },
                            {
                                title: "Birthdate",
                                data: "tgl"
                            },
                            {
                                title: "Birthplace",
                                data: "tempatLahir"
                            },
                            {
                                title: "Age",
                                data: "umur"
                            },
                            {
                                title: "Pendidikan Terakhir",
                                data: "pendidikanTerakhir"
                            }
                        ]
                    });
                } else if (result[0].status == "true1") {
                    Swal.fire(
                        'Cannot be found',
                        'ID Number "' + $('#nik').val() + '" does not exist',
                        'warning'
                    )
                } else if (result[0].status == "false") {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Your Request Cannot be Processed',
                        footer: '<h6>ID Number must be 16 digits</h6>'
                    })
                }
            },
            error: function (err) {
                console.log(err);
            }
        })
    },
    setEditData: function (id) {
        formBiodata.resetForm();
        $.ajax({
            url: '/biodata/edit/' + id,
            method: 'get',
            contentType: 'application/json',
            dataType: 'json',
            success: function (res, status, xhr) {
                if (xhr.status == 200 || xhr.status == 201) {
                    $('#form-biodata').fromJSON(JSON.stringify(res));
                    $('#modal-biodata').modal('show')
                    editbio = 1;
                } else {

                }
            },
            erorrr: function (err) {
                console.log(err);
            }
        });
    },
    setDeleteData: function (id) {
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/biodata/' + id,
                    method: 'delete',
                    contentType: 'application/json',
                    dataType: 'json'
                })
                Swal.fire(
                    'Deleted!',
                    'Your file has been deleted.',
                    'success'
                )
                tableBiodata.create();
            }
        })

    }
};