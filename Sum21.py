class Node:
    def __init__(self):
        self.children = []
        self.moves = []
        self.score_of_move = []


def minimax(node, depth, sum):
    if game_over(sum):
        score = get_score(sum, depth)
        node.score_of_move.append(score)
        node.moves.clear()
        return score

    score = 0

    for move in node.moves:
        child = Node()
        mvs = [moves for moves in node.moves if moves is not move]
        child.moves.extend(mvs)
        node.children.append(child)
        score = minimax(child, depth + 1, sum + move)
        node.score_of_move.append(apt_score(depth, child))

    return score


def game_over(sum):
    return sum >= 21


def get_score(sum, depth):
    if sum > 21:
        if depth % 2 is not 0:
            return 10 - depth
        else:
            return depth - 10
    else:
        return 0


def apt_score(depth, node):
    if depth%2 is not 0:
        return min(node.score_of_move)
    return max(node.score_of_move)


def ai_move(node):
    min_index = -1
    min = 100
    global depth, root
    depth = depth + 1
    for i in range(0, len(node.moves)):
        if node.score_of_move[i] < min:
            min = node.score_of_move[i]
            min_index = i
    root = node.children[min_index]
    return node.moves[min_index]


def player_move(node, move):
    global depth, root
    depth += 1
    for i in range(0, len(node.moves)):
        if move == moves:
            root = node.children[i]
            return


moves = [i for i in range(1, 10)]
depth = 0
sum = 0
root = Node()


def introduce_rules():
    print("\t\t----------")
    print("\t\t| SUM 21 |")
    print("\t\t----------")
    print(" Rules:")
    print(" 1. Game will be played between you and the computer: ")
    print(" 2. You can select a number between 1 and 9 without repetition.")
    print(" 3. If the sum of all selected numbers is greater than 21, the last player to select the number loses.")
    print(" 4. If the sum is equal to 21, it will be a tie.")
    print(" 5. If you enter an invalid move, you will be prompted to enter again.")
    print(" Press enter if ready.")


def validate_move(move):
    count = moves.count(move)
    if count > 0:
        return True
    else:
        return False


class _main_:
    move = 0
    introduce_rules()
    input()
    while True:
        try:
            while not validate_move(move) :
                move = int(input("Your move:  "))
            break
        except:
            print("Please enter number only")
    moves.remove(move)
    root.moves.extend(moves)
    minimax(root, depth, move)
    sum += move

    while sum < 21:
        if depth % 2 is not 0:         # Players turn
            print("\t\tCurrent Sum:  " + str(sum))
            print("\t\tAvailable moves: ", moves)
            while True:
                try:
                    while not validate_move(move):
                        move = int(input("Your move:  "))
                    player_move(root, move)
                    break
                except:
                    print("Please enter number only")
        else:                           # AI turn
            move = ai_move(root)
            print(" AI move:  ", move)
        moves.remove(move)
        sum += move
    if sum > 21:
        if depth % 2 is 0:
            print("AI wins!!!")
        else:
            print("You win!!!")
    else:
        print("Its a tie:(")
