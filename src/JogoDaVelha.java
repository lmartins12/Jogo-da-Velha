import java.util.Arrays;
import java.util.Scanner;

public class JogoDaVelha {
    private static final char[][] tabuleiro = new char[3][3];
    private static char jogador = 'X';
    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        boolean jogarDeNovo = true;
        while (jogarDeNovo) {
            inicializarTabuleiro();
            exibirTabuleiro();

            while (!verificarFimDeJogo()) {
                jogar();
                exibirTabuleiro();
                trocarJogador();
            }

            char vencedor = verificarVencedor();
            if (vencedor == ' ') {
                System.out.println("O jogo terminou empatado!");
            } else {
                System.out.println("O jogador " + vencedor + " venceu!");
            }

            jogarDeNovo = jogarNovamente();
        }
    }

    private static void inicializarTabuleiro() {
        for (int i = 0; i < 3; i++) {
            Arrays.fill(tabuleiro[i], ' ');
        }
    }

    private static void exibirTabuleiro() {
        System.out.println("  1 2 3");
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean verificarFimDeJogo() {
        char vencedor = verificarVencedor();
        if (vencedor != ' ') {
            return true;
        }

        for (char[] linha : tabuleiro) {
            for (char celula : linha) {
                if (celula == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    private static char verificarVencedor() {
        for (int i = 0; i < 3; i++) {
            // Verificar linhas
            if (tabuleiro[i][0] != ' ' && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
                return tabuleiro[i][0];
            }
            // Verificar colunas
            if (tabuleiro[0][i] != ' ' && tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
                return tabuleiro[0][i];
            }
        }
        // Verificar diagonais
        if (tabuleiro[0][0] != ' ' && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            return tabuleiro[0][0];
        }
        if (tabuleiro[0][2] != ' ' && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            return tabuleiro[0][2];
        }
        return ' ';
    }

    private static void jogar() {
        char jogadorAtual = jogador;

        while (!verificarFimDeJogo()) {
            imprimirTabuleiro();
            System.out.println("Sua vez");
            if (jogadorAtual == 'X') {
                jogadaDoUsuario();
            } else {
                jogadaDaIA();
            }
            trocarJogador();
            jogadorAtual = jogador;
        }

        imprimirTabuleiro();
        char vencedor = verificarVencedor();
        if (vencedor != ' ') {
            System.out.println("O jogador " + vencedor + " venceu!");
        } else {
            System.out.println("Empate!");
        }
    }

    private static void jogadaDoUsuario() {
        System.out.print("Digite a linha: ");
        int linha = entrada.nextInt() - 1;
        System.out.print("Digite a coluna: ");
        int coluna = entrada.nextInt() - 1;

        if (linha < 0 || linha > 2 || coluna < 0 || coluna > 2) {
            System.out.println("Jogada inválida! Tente novamente.");
            jogadaDoUsuario();
        } else if (tabuleiro[linha][coluna] != ' ') {
            System.out.println("Posição já ocupada! Tente novamente.");
            jogadaDoUsuario();
        } else {
            tabuleiro[linha][coluna] = jogador;
        }
    }

    private static void jogadaDaIA() {
        System.out.println("Vez da IA...");
        int[] posicao = minimax(2, 'O');
        tabuleiro[posicao[0]][posicao[1]] = jogador;
    }

    private static int[] minimax(int profundidade, char jogadorAtual) {
        int[] melhorJogada = {-1, -1};
        if (profundidade == 0 || verificarFimDeJogo()) {
            int pontuacao = avaliarTabuleiro();
            return new int[]{pontuacao, -1, -1};
        }

        int melhorPontuacao = jogadorAtual == 'O' ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == ' ') {
                    tabuleiro[i][j] = jogadorAtual;
                    int pontuacao = minimax(profundidade - 1, jogadorAtual == 'O' ? 'X' : 'O')[0];
                    if ((jogadorAtual == 'O' && pontuacao > melhorPontuacao) || (jogadorAtual == 'X' && pontuacao < melhorPontuacao)) {
                        melhorPontuacao = pontuacao;
                        melhorJogada[0] = i;
                        melhorJogada[1] = j;
                    }
                    tabuleiro[i][j] = ' ';
                }
            }
        }
        return melhorJogada;
    }

    private static int avaliarTabuleiro() {
        int pontuacao = 0;
        pontuacao += avaliarLinha(0, 0, 0, 1, 0, 2);
        pontuacao += avaliarLinha(1, 0, 1, 1, 1, 2);
        pontuacao += avaliarLinha(2, 0, 2, 1, 2, 2);
        pontuacao += avaliarLinha(0, 0, 1, 0, 2, 0);
        pontuacao += avaliarLinha(0, 1, 1, 1, 2, 1);
        pontuacao += avaliarLinha(0, 2, 1, 2, 2, 2);
        pontuacao += avaliarLinha(0, 0, 1, 1, 2, 2);
        pontuacao += avaliarLinha(0, 2, 1, 1, 2, 0);
        return pontuacao;
    }

    private static int avaliarLinha(int l1, int c1, int l2, int c2, int l3, int c3) {
        int pontuacao = 0;

        if (tabuleiro[l1][c1] == 'X') {
            pontuacao = 1;
        } else if (tabuleiro[l1][c1] == 'O') {
            pontuacao = -1;
        }

        if (tabuleiro[l2][c2] == 'X') {
            if (pontuacao == 1) {
                pontuacao = 10;
            } else if (pontuacao == -1) {
                return 0;
            } else {
                pontuacao = 1;
            }
        } else if (tabuleiro[l2][c2] == 'O') {
            if (pontuacao == -1) {
                pontuacao = -10;
            } else if (pontuacao == 1) {
                return 0;
            } else {
                pontuacao = -1;
            }
        }

        if (tabuleiro[l3][c3] == 'X') {
            if (pontuacao > 0) {
                pontuacao *= 10;
            } else if (pontuacao < 0) {
                return 0;
            } else {
                pontuacao = 1;
            }
        } else if (tabuleiro[l3][c3] == 'O') {
            if (pontuacao < 0) {
                pontuacao *= 10;
            } else if (pontuacao > 1) {
                return 0;
            } else {
                pontuacao = -1;
            }
        }

        return pontuacao;
    }

    private static void trocarJogador() {
        jogador = jogador == 'X' ? 'O' : 'X';
    }

    private static void imprimirTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean jogarNovamente() {
        System.out.println("Deseja jogar novamente? (s/n)");
        String resposta = entrada.next();
        return resposta.equalsIgnoreCase("s");
    }
}