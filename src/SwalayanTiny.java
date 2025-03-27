import java.util.Scanner;

class Pelanggan {
    // Atribut private untuk encapsulation
    private String nomorPelanggan;
    private String nama;
    private double saldo;
    private String pin;
    private int kesalahanAuth;
    private boolean frozen;

    // Constructor untuk inisialisasi objek Pelanggan
    public Pelanggan(String nomorPelanggan, String nama, double saldo, String pin) {
        this.nomorPelanggan = nomorPelanggan;
        this.nama = nama;
        this.saldo = saldo;
        this.pin = pin;
        this.kesalahanAuth = 0;  // Inisialisasi kesalahan auth ke 0
        this.frozen = false;      // Awalnya akun tidak dibekukan
    }

    // Getter methods untuk mengakses atribut private
    public String getNomorPelanggan() {
        return nomorPelanggan;
    }

    public String getNama() {
        return nama;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isFrozen() {
        return frozen;
    }

    // Method untuk autentikasi dengan PIN
    public boolean autentikasi(String pinMasukan) {
        if (frozen) {
            System.out.println("Akun Anda dibekukan karena terlalu banyak kesalahan autentikasi.");
            return false;
        }
        
        if (pin.equals(pinMasukan)) {
            kesalahanAuth = 0; // Reset kesalahan jika berhasil
            return true;
        } else {
            kesalahanAuth++;
            System.out.println("PIN salah. Kesalahan ke-" + kesalahanAuth);
            
            if (kesalahanAuth >= 3) {
                frozen = true;  // Bekukan akun setelah 3x salah
                System.out.println("Akun Anda dibekukan karena terlalu banyak kesalahan autentikasi.");
            }
            return false;
        }
    }

    // Method untuk top up saldo
    public void topUp(double jumlah) {
        if (frozen) {
            System.out.println("Akun dibekukan, tidak bisa melakukan top up.");
            return;
        }
        
        if (jumlah <= 0) {
            System.out.println("Jumlah top up harus lebih dari 0.");
            return;
        }
        
        saldo += jumlah;  // Tambahkan jumlah ke saldo
        System.out.println("Top up berhasil. Saldo baru: Rp" + saldo);
    }

    // Method untuk pembelian
    public void pembelian(double jumlah) {
        if (frozen) {
            System.out.println("Akun dibekukan, tidak bisa melakukan pembelian.");
            return;
        }
        
        if (jumlah <= 0) {
            System.out.println("Jumlah pembelian harus lebih dari 0.");
            return;
        }
        
        // Cek saldo minimal setelah transaksi
        double saldoSetelahTransaksi = saldo - jumlah;
        if (saldoSetelahTransaksi < 10000) {
            System.out.println("Transaksi gagal. Saldo minimal Rp10.000 harus terjaga.");
            return;
        }
        
        // Hitung cashback berdasarkan jenis pelanggan
        double cashback = hitungCashback(jumlah);
        
        // Lakukan transaksi (kurangi jumlah belanja dan tambahkan cashback)
        saldo = saldo - jumlah + cashback;
        
        System.out.println("Pembelian berhasil.");
        System.out.println("Total belanja: Rp" + jumlah);
        if (cashback > 0) {
            System.out.println("Anda mendapat cashback: Rp" + cashback);
        }
        System.out.println("Saldo baru: Rp" + saldo);
    }

    // Method private untuk menghitung cashback
    private double hitungCashback(double jumlah) {
        String jenis = nomorPelanggan.substring(0, 2);  // Ambil 2 digit pertama
        double cashback = 0;
        
        // Hitung cashback berdasarkan jenis pelanggan
        if (jenis.equals("38")) { // Silver
            if (jumlah > 1000000) {
                cashback = jumlah * 0.05;
            }
        } else if (jenis.equals("56")) { // Gold
            if (jumlah > 1000000) {
                cashback = jumlah * 0.07;
            } else {
                cashback = jumlah * 0.02;
            }
        } else if (jenis.equals("74")) { // Platinum
            if (jumlah > 1000000) {
                cashback = jumlah * 0.10;
            } else {
                cashback = jumlah * 0.05;
            }
        }
        
        return cashback;
    }
}

public class SwalayanTiny { 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Inisialisasi data pelanggan
        Pelanggan[] daftarPelanggan = {
            new Pelanggan("3812345678", "Budi Silver", 500000, "1234"),
            new Pelanggan("5612345678", "Wanto Gold", 1500000, "5678"),
            new Pelanggan("7412345678", "Cindel Platinum", 3000000, "9012")
        };
        
        // Tampilkan header sistem
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║        SWALAYAN TINY         ║");
        System.out.println("╚══════════════════════════════╝");
        
        // Loop utama program
        while (true) {
            // Tampilkan menu utama
            System.out.println("\nMENU UTAMA:");
            System.out.println("1. Informasi Akun");
            System.out.println("2. Transaksi Pembelian");
            System.out.println("3. Isi Ulang Saldo");
            System.out.println("4. Keluar");
            System.out.print("Pilihan Anda: ");
            
            int menu = scanner.nextInt();
            scanner.nextLine();  // Membersihkan newline
            
            // Keluar dari program jika memilih menu 4
            if (menu == 4) {
                System.out.println("\nTerima kasih telah berbelanja di Swalayan Tiny!");
                break;
            }
            
            // Input nomor pelanggan dan PIN
            System.out.print("\nMasukkan Nomor Pelanggan: ");
            String nomor = scanner.nextLine();
            
            System.out.print("Masukkan PIN: ");
            String pin = scanner.nextLine();
            
            // Cari pelanggan berdasarkan nomor
            Pelanggan pelangganAktif = null;
            for (Pelanggan p : daftarPelanggan) {
                if (p.getNomorPelanggan().equals(nomor)) {
                    pelangganAktif = p;
                    break;
                }
            }
            
            // Jika pelanggan tidak ditemukan
            if (pelangganAktif == null) {
                System.out.println("\n Nomor pelanggan tidak ditemukan!");
                continue;
            }
            
            // Autentikasi PIN
            if (!pelangganAktif.autentikasi(pin)) {
                continue;
            }
            
            // Proses menu yang dipilih
            switch (menu) {
                case 1: // Informasi Akun
                    System.out.println("\n════════ INFORMASI AKUN ════════");
                    System.out.println("Nama Pelanggan : " + pelangganAktif.getNama());
                    System.out.println("No. Pelanggan  : " + pelangganAktif.getNomorPelanggan());
                    
                    // Tentukan jenis membership
                    String jenis = pelangganAktif.getNomorPelanggan().substring(0, 2);
                    String membership = "";
                    if (jenis.equals("38")) membership = "SILVER (Cashback 5% >1jt)";
                    else if (jenis.equals("56")) membership = "GOLD (Cashback 7% >1jt, 2% lainnya)";
                    else if (jenis.equals("74")) membership = "PLATINUM (Cashback 10% >1jt, 5% lainnya)";
                    
                    System.out.println("Membership     : " + membership);
                    System.out.println("Saldo          : Rp" + String.format("%,.2f", pelangganAktif.getSaldo()));
                    System.out.println("Status Akun    : " + (pelangganAktif.isFrozen() ? " DIBLOKIR" : " AKTIF"));
                    System.out.println("═══════════════════════════════");
                    break;
                    
                case 2: // Transaksi Pembelian
                    System.out.print("\nMasukkan Total Belanja: Rp");
                    double belanja = scanner.nextDouble();
                    scanner.nextLine();  // Membersihkan newline
                    pelangganAktif.pembelian(belanja);
                    break;
                    
                case 3: // Isi Ulang Saldo
                    System.out.print("\nMasukkan Jumlah Top Up: Rp");
                    double topup = scanner.nextDouble();
                    scanner.nextLine();  // Membersihkan newline
                    pelangganAktif.topUp(topup);
                    break;
                    
                default:
                    System.out.println("\n Menu tidak valid!");
            }
        }
    }
}